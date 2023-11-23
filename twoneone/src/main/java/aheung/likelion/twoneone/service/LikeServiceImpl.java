package aheung.likelion.twoneone.service;

import aheung.likelion.twoneone.domain.community.Post;
import aheung.likelion.twoneone.domain.community.PostLike;
import aheung.likelion.twoneone.domain.user.User;
import aheung.likelion.twoneone.dto.community.PostListReturnDto;
import aheung.likelion.twoneone.dto.file.FileListDto;
import aheung.likelion.twoneone.repository.PostLikeRepository;
import aheung.likelion.twoneone.repository.PostRepository;
import aheung.likelion.twoneone.repository.UserRepository;
import com.amazonaws.services.kms.model.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FileService fileService;

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
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
    public void createPostLike(Long postId, Long userId) {
        User user = findUser(userId);
        Post post = findPost(postId);

        if (postLikeRepository.existsByPostAndUser(post, user)) {
            throw new UnsupportedOperationException();
        }

        postLikeRepository.save(PostLike.builder()
                .post(post)
                .user(user)
                .build());

        post.updateLikes(post.getLikes() + 1);

        postRepository.save(post);
    }

    @Transactional
    @Override
    public void deletePostLike(Long postId, Long userId) {
        User user = findUser(userId);
        Post post = findPost(postId);

        PostLike like = postLikeRepository.findByPostAndUser(post, user)
                .orElseThrow(() -> new NotFoundException("Not Found Post Like"));

        postLikeRepository.delete(like);

        post.updateLikes(post.getLikes() - 1);

        postRepository.save(post);
    }

    @Transactional
    @Override
    public Page<PostListReturnDto> getLikePosts(Pageable pageable, Long userId) {
        User user = findUser(userId);

        List<PostLike> likes = postLikeRepository.findByUser(user);

        List<PostListReturnDto> posts = likes.stream().map(like -> {
            Post post = like.getPost();
            FileListDto files = fileService.getFiles("post", post.getId());
            return PostListReturnDto.toDto(post, files.getThumbnail(), true);
        }).toList();

        // List -> Page
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), posts.size());
        return new PageImpl<>(posts.subList(start, end), pageable, posts.size());
    }
}
