package aheung.likelion.twoneone.controller;

import aheung.likelion.twoneone.dto.common.ReturnDto;
import aheung.likelion.twoneone.dto.community.PostListReturnDto;
import aheung.likelion.twoneone.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ReturnDto<Void> createPostLike(@PathVariable Long postId) {
        likeService.createPostLike(postId);
        return ReturnDto.ok();
    }

    @DeleteMapping("/likes/posts/{postId}")
    public ReturnDto<Void> deletePostLike(@PathVariable Long postId) {
        likeService.deletePostLike(postId);
        return ReturnDto.ok();
    }

    @GetMapping("/likes/posts")
    public ReturnDto<Page<PostListReturnDto>> getLikePosts(@PageableDefault Pageable pageable) {
        return ReturnDto.ok(likeService.getLikePosts(pageable));
    }
}