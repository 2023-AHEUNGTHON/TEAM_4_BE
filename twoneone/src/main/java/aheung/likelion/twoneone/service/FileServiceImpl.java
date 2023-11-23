package aheung.likelion.twoneone.service;

import aheung.likelion.twoneone.domain.common.File;
import aheung.likelion.twoneone.dto.file.FileDto;
import aheung.likelion.twoneone.dto.file.FileListDto;
import aheung.likelion.twoneone.repository.FileRepository;
import aheung.likelion.twoneone.util.S3Uploader;
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

    @Transactional
    @Override
    public FileListDto getFiles(String table, Long id) {

        return FileListDto.builder()
                .files(fileRepository.findByTargetTableAndTargetId(table, id).stream()
                        .map(FileDto::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional
    @Override
    public void deleteFiles(String table, Long id) {
        List<File> oldFiles = fileRepository.findByTargetTableAndTargetId(table, id);

        for(File file : oldFiles) {
            // TODO : S3 파일 삭제 안되는 것 수정해야 함
            fileUploader.deleteFile(file.getUrl());
        }

        fileRepository.deleteAll(oldFiles);
    }


}
