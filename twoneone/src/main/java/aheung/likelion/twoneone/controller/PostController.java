package aheung.likelion.twoneone.controller;

import aheung.likelion.twoneone.dto.common.ReturnDto;
import aheung.likelion.twoneone.dto.community.PostRequestDto;
import aheung.likelion.twoneone.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public ReturnDto<Void> createPost(@RequestPart PostRequestDto dto,
                                      @RequestPart(required = false) List<MultipartFile> files,
                                      @RequestParam(name = "user_id") Long userId) {
        postService.createPost(dto, files, userId);
        return ReturnDto.ok();
    }

}
