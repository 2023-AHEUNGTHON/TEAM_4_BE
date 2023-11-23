package aheung.likelion.twoneone.dto.community;

import aheung.likelion.twoneone.domain.community.Post;
import aheung.likelion.twoneone.domain.enums.Category;
import aheung.likelion.twoneone.dto.file.FileListReturnDto;
import aheung.likelion.twoneone.dto.file.FileReturnDto;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostDetailReturnDto {


    private Long id;
    private Category category;
    private String title;
    private String memo;
    private String link;
    private List<FileReturnDto> files;
    private String createdAt;

    public static PostDetailReturnDto toDto(Post post, FileListReturnDto files) {
        return PostDetailReturnDto.builder()
                .id(post.getId())
                .category(post.getCategory())
                .title(post.getTitle())
                .memo(post.getMemo())
                .link(post.getLink())
                .createdAt(post.getCreatedAtToString())
                .files(files.getFiles())
                .build();
    }
}