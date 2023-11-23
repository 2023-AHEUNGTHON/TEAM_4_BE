package aheung.likelion.twoneone.service;

import aheung.likelion.twoneone.dto.community.PostListReturnDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LikeService {
    public void createPostLike(Long postId, Long userId);
    public void deletePostLike(Long postId, Long userId);
    public Page<PostListReturnDto> getLikePosts(Pageable pageable, Long userId);
}
