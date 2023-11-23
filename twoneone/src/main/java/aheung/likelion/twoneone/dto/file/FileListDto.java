package aheung.likelion.twoneone.dto.file;

import aheung.likelion.twoneone.domain.enums.FileType;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileListDto {
    List<FileDto> files;

    public String getThumbnail() {
        for (FileDto file : files) {
            if (file.getType().equals(FileType.IMAGE)) {
                return file.getUrl();
            }
        }

        return null;
    }
}
