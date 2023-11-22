package aheung.likelion.twoneone.dto.file;

import aheung.likelion.twoneone.domain.common.File;
import aheung.likelion.twoneone.domain.enums.FileType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileReturnDto {
    private Long id;
    private Long size;
    private FileType type;
    private String url;
    private String createdAt;

    public static FileReturnDto toDto(File file) {
        return FileReturnDto.builder()
                .id(file.getId())
                .size(file.getSize())
                .type(FileType.from(file.getType()))
                .url(file.getUrl())
                .createdAt(file.getCreatedAtToString())
                .build();
    }
}
