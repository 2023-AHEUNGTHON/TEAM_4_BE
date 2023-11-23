package aheung.likelion.twoneone.controller;

import aheung.likelion.twoneone.dto.common.ReturnDto;
import aheung.likelion.twoneone.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/likes/posts/{postId}")
    public ReturnDto<Void> createPostLike(@PathVariable Long postId,
                                          @RequestParam(name = "user_id") Long userId) {
        likeService.createPostLike(postId, userId);
        return ReturnDto.ok();
    }

    @DeleteMapping("/likes/posts/{postId}")
    public ReturnDto<Void> deletePostLike(@PathVariable Long postId,
                                          @RequestParam(name = "user_id") Long userId) {
        likeService.deletePostLike(postId, userId);
        return ReturnDto.ok();
    }
}
