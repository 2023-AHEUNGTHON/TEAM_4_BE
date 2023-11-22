package aheung.likelion.twoneone.dto.file;

import aheung.likelion.twoneone.domain.enums.FileType;
import aheung.likelion.twoneone.dto.file.FileReturnDto;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileListReturnDto {
    List<FileReturnDto> files;

    public String getThumbnail() {
        for (FileReturnDto file : files) {
            if (file.getType().equals(FileType.IMAGE)) {
                return file.getUrl();
            }
        }

        return null;
    }
}
