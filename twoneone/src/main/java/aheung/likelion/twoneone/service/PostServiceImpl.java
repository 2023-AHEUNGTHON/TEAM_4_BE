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
import aheung.likelion.twoneone.repository.PostTagRepository;
import aheung.likelion.twoneone.repository.TagRepository;
import aheung.likelion.twoneone.repository.UserRepository;
import aheung.likelion.twoneone.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
                    throw new IllegalArgumentException();
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
                    throw new IllegalArgumentException();
                }
        );
    }

    @Transactional
    @Override
    public void createPost(PostRequestDto dto, List<MultipartFile> files, Long userId) {
        User user = findUser(userId);
        Post post = postRepository.save(dto.toEntity(user));

        // TODO : Post와 Tag 엮기
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
    public Page<PostListReturnDto> getMyPosts(Pageable pageable, String category, String tagName, Long userId) {
        User user = findUser(userId);
        Tag tag = findTag(tagName);
        List<Post> posts = postRepository.getPostByCategoryAndUser(Category.from(category), user);

        List<PostListReturnDto> myPosts = new ArrayList<>();
        for (Post post : posts) {
            FileListDto files = fileService.getFiles("post", post.getId());
            if (postTagRepository.existsByPostAndTag(post, tag)) {
                myPosts.add(PostListReturnDto.toDto(post, files.getThumbnail()));
            }

        }

        // List -> Page
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), myPosts.size());
        return new PageImpl<>(myPosts.subList(start, end), pageable, myPosts.size());
    }

    @Transactional
    @Override
    public Page<PostListReturnDto> getMySearchPosts(Pageable pageable, String keyword, Long userId) {
        User user = findUser(userId);

        List<Post> posts = postRepository.getPostByTitleContainingAndUser(keyword, user);
        List<PostListReturnDto> myPosts = new ArrayList<>();
        for (Post post : posts) {
            FileListDto files = fileService.getFiles("post", post.getId());
            myPosts.add(PostListReturnDto.toDto(post, files.getThumbnail()));
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
        FileListDto files = fileService.getFiles("post", post.getId());
        return PostDetailReturnDto.toDto(post, files);
    }

    @Transactional
    @Override
    public void updatePost(PostRequestDto dto, List<MultipartFile> files, Long postId) {
        Post post = findPost(postId);
        fileService.deleteFiles("post", post.getId());

        post.updatePost(dto);
        fileService.createFiles("post", post.getId(), files);

        postRepository.save(post);
    }

}
