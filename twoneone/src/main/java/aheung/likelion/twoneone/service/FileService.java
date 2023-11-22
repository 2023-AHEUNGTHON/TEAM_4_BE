package aheung.likelion.twoneone.service;

import aheung.likelion.twoneone.dto.file.FileListReturnDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    public void createFiles(String table, Long id, List<MultipartFile> files);
    public FileListReturnDto getFiles(String table, Long id);
}
