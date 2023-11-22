package aheung.likelion.twoneone.service;

import aheung.likelion.twoneone.dto.community.PostRequestDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface PostService {
    public void createPost(PostRequestDto dto, List<MultipartFile> files, Long userId);
}
