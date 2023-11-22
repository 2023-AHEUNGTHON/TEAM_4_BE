package aheung.likelion.twoneone.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    public void createFiles(String table, Long id, List<MultipartFile> files);
}
