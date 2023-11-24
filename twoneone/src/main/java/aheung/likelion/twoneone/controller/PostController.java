package aheung.likelion.twoneone.controller;

import aheung.likelion.twoneone.dto.common.ReturnDto;
import aheung.likelion.twoneone.dto.community.PostDetailReturnDto;
import aheung.likelion.twoneone.dto.community.PostListReturnDto;
import aheung.likelion.twoneone.dto.community.PostRequestDto;
import aheung.likelion.twoneone.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public ReturnDto<Void> createPost(@RequestPart PostRequestDto dto,
                                      @RequestPart(required = false) List<MultipartFile> files) {
        postService.createPost(dto, files);
        return ReturnDto.ok();
    }

    @GetMapping("/my/{category}/posts")
    public ReturnDto<Page<PostListReturnDto>> getMyPosts(@PathVariable String category,
                                                         @RequestParam String tag,
                                                         @PageableDefault(size = 12) Pageable pageable) {

        return ReturnDto.ok(postService.getMyPosts(pageable, category, tag));
    }

    @GetMapping("/my/posts")
    public ReturnDto<Page<PostListReturnDto>> getMySearchPosts(@RequestParam String keyword,
                                                               @PageableDefault(size = 12) Pageable pageable) {
        return ReturnDto.ok(postService.getMySearchPosts(pageable, keyword));
    }

    @GetMapping("/posts")
    public ReturnDto<Page<PostListReturnDto>> getPosts(@RequestParam(required = false) String keyword,
                                                       @PageableDefault(size = 12) Pageable pageable) {
        return ReturnDto.ok(postService.getPosts(pageable, keyword));
    }

    @GetMapping("/posts/{postId}")
    public ReturnDto<PostDetailReturnDto> getPost(@PathVariable Long postId) {
        return ReturnDto.ok(postService.getPost(postId));
    }

    @PutMapping("/posts/{postId}")
    public ReturnDto<Void> updatePost(@RequestPart PostRequestDto dto,
                                      @RequestPart(required = false) List<MultipartFile> files,
                                      @PathVariable Long postId) {
        postService.updatePost(dto, files, postId);
        return ReturnDto.ok();
    }

    @DeleteMapping("/posts/{postId}")
    public ReturnDto<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ReturnDto.ok();
    }

}
