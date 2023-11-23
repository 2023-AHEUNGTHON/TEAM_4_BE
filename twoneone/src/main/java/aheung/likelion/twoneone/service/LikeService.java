package aheung.likelion.twoneone.service;

public interface LikeService {
    public void createPostLike(Long postId, Long userId);
    public void deletePostLike(Long postId, Long userId);
}
