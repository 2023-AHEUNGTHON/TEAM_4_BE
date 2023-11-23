package aheung.likelion.twoneone.service;

import aheung.likelion.twoneone.domain.community.Post;
import aheung.likelion.twoneone.domain.community.PostTag;
import aheung.likelion.twoneone.domain.community.Tag;
import aheung.likelion.twoneone.domain.enums.Category;
import aheung.likelion.twoneone.domain.enums.Tags;
import aheung.likelion.twoneone.domain.user.User;
import aheung.likelion.twoneone.dto.community.PostDetailReturnDto;
import aheung.likelion.twoneone.dto.file.FileListDto;
import aheung.likelion.twoneone.dto.community.PostListReturnDto;
import aheung.likelion.twoneone.dto.community.PostRequestDto;
import aheung.likelion.twoneone.exception.AppException;
import aheung.likelion.twoneone.exception.ErrorCode;
import aheung.likelion.twoneone.repository.PostLikeRepository;
import aheung.likelion.twoneone.repository.PostTagRepository;
import aheung.likelion.twoneone.repository.TagRepository;
import aheung.likelion.twoneone.repository.UserRepository;
import aheung.likelion.twoneone.repository.PostRepository;
import aheung.likelion.twoneone.util.AuthUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final FileService fileService;


    private User findUser(String username) {
        return userRepository.findByUserName(username).orElseThrow(() -> {
                    throw new AppException(ErrorCode.NOT_FOUND, "GET User");
                }
        );
    }

    private Tag findTag(String tagName) {
        return tagRepository.findByName(tagName).orElseThrow(() -> {
                    throw new IllegalArgumentException();
                }
        );
    }
    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() -> {
                    throw new AppException(ErrorCode.NOT_FOUND, "GET Post");
                }
        );
    }

    @Transactional
    @Override
    public void createPost(PostRequestDto dto, List<MultipartFile> files) {
        User user = findUser(AuthUtil.getAuthUser());
        Post post = postRepository.save(dto.toEntity(user));

        if (files != null) {
            fileService.createFiles("post", post.getId(), files);
            Tag tag = findTag(Tags.FILE.getEn());
            postTagRepository.save(PostTag.builder()
                    .post(post)
                    .tag(tag)
                    .build());
        }

        if (post.getLink() != null) {
            Tag tag = findTag(Tags.LINK.getEn());
            postTagRepository.save(PostTag.builder()
                    .post(post)
                    .tag(tag)
                    .build());
        }

        if (post.getMemo() != null) {
            Tag tag = findTag(Tags.MEMO.getEn());
            postTagRepository.save(PostTag.builder()
                    .post(post)
                    .tag(tag)
                    .build());
        }
    }

    @Transactional
    @Override
    public Page<PostListReturnDto> getMyPosts(Pageable pageable, String category, String tagName) {
        User user = findUser(AuthUtil.getAuthUser());
        Tag tag = findTag(tagName);
        List<Post> posts = postRepository.getPostByCategoryAndUser(Category.from(category), user, pageable.getSort());

        List<PostListReturnDto> myPosts = new ArrayList<>();
        for (Post post : posts) {
            FileListDto files = fileService.getFiles("post", post.getId());
            if (postTagRepository.existsByPostAndTag(post, tag)) {
                myPosts.add(PostListReturnDto.toDto(
                        post,
                        files.getThumbnail(),
                        postLikeRepository.existsByPostAndUser(post, user)));
            }
        }

        // List -> Page
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), myPosts.size());
        return new PageImpl<>(myPosts.subList(start, end), pageable, myPosts.size());
    }

    @Transactional
    @Override
    public Page<PostListReturnDto> getMySearchPosts(Pageable pageable, String keyword) {
        User user = findUser(AuthUtil.getAuthUser());

        List<PostListReturnDto> myPosts = getPostsWithFile(
                postRepository.getPostByTitleContainingAndUser(keyword, user, pageable.getSort()),
                user
        );

        // List -> Page
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), myPosts.size());
        return new PageImpl<>(myPosts.subList(start, end), pageable, myPosts.size());
    }

    @Transactional
    @Override
    public Page<PostListReturnDto> getPosts(Pageable pageable, String keyword) {
        User user = findUser(AuthUtil.getAuthUser());

        List<PostListReturnDto> myPosts;
        if (keyword != null) {
            myPosts = getPostsWithFile(
                    postRepository.getPostByTitleContainingAndShareIsTrue(keyword, pageable.getSort()),
                    user
            );
        } else {
            myPosts = getPostsWithFile(
                    postRepository.getPostByShareIsTrue(pageable.getSort()),
                    user
            );
        }

        // List -> Page
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), myPosts.size());
        return new PageImpl<>(myPosts.subList(start, end), pageable, myPosts.size());
    }

    @Transactional
    @Override
    public PostDetailReturnDto getPost(Long postId) {
        Post post = findPost(postId);
        User user = findUser(AuthUtil.getAuthUser());

        FileListDto files = fileService.getFiles("post", post.getId());
        return PostDetailReturnDto.toDto(
                post,
                files,
                postLikeRepository.existsByPostAndUser(post, user)
        );
    }

    @Transactional
    @Override
    public void updatePost(PostRequestDto dto, List<MultipartFile> files, Long postId) {
        Post post = findPost(postId);
        User user = findUser(AuthUtil.getAuthUser());

        if (!post.getUser().equals(user)) {
            throw new AppException(ErrorCode.FORBIDDEN, "Update Post");
        }

        fileService.deleteFiles("post", post.getId());

        post.updatePost(dto);
        fileService.createFiles("post", post.getId(), files);

        postRepository.save(post);
    }

    @Transactional
    @Override
    public void deletePost(Long postId) {
        Post post = findPost(postId);
        User user = findUser(AuthUtil.getAuthUser());

        if (!post.getUser().equals(user)) {
            throw new AppException(ErrorCode.FORBIDDEN, "Delete Post");
        }

        fileService.deleteFiles("post", post.getId());
        List<PostTag> postTags = postTagRepository.findByPost(post);

        postTagRepository.deleteAll(postTags);
        postRepository.delete(post);
    }

    private List<PostListReturnDto> getPostsWithFile(List<Post> posts, User user) {
        return posts.stream().map(post -> {
            FileListDto files = fileService.getFiles("post", post.getId());
            return PostListReturnDto.toDto(
                    post,
                    files.getThumbnail(),
                    postLikeRepository.existsByPostAndUser(post, user));
        }).collect(Collectors.toList());
    }
}
