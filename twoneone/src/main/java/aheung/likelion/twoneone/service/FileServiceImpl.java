package aheung.likelion.twoneone.service;

import aheung.likelion.twoneone.domain.common.File;
import aheung.likelion.twoneone.dto.FileReturnDto;
import aheung.likelion.twoneone.repository.FileRepository;
import aheung.likelion.twoneone.util.S3Uploader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final S3Uploader fileUploader;
    private final FileRepository fileRepository;

    @Transactional
    @Override
    public void createFiles(String table, Long id, List<MultipartFile> files) {
        for (MultipartFile file : files) {
            String url = fileUploader.uploadFile(file);

            fileRepository.save(File.builder()
                    .targetTable(table)
                    .targetId(id)
                    .type(file.getContentType())
                    .size(file.getSize())
                    .url(url)
                    .build());
        }
    }
}
