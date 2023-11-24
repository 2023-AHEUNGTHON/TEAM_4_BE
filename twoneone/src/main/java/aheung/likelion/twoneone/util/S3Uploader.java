package aheung.likelion.twoneone.util;

import aheung.likelion.twoneone.dto.file.FileListDto;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Uploader {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<String> uploadMultipleFile(List<MultipartFile> files) {
        List<String> urls = new ArrayList<>();

        for (MultipartFile file : files) {
            urls.add(uploadFile(file));
        }

        return urls;
    }

    public String uploadFile(MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return uploadS3(file);
    }

    private String uploadS3(MultipartFile file) {
        // TODO : 원본 파일 이름 추가시 인코딩 디코딩 ?
        String filename = createFileName();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, filename, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new MultipartException("s3 upload error");
        }

        return amazonS3.getUrl(bucket, filename).toString();
    }

    public void deleteFile(String url) {
        if(StringUtils.hasText(url)) {
            String key = url.replace(amazonS3.getUrl(bucket, "").toString(), "").substring(1);
            amazonS3.deleteObject(bucket, key);
         }
    }
    private String createFileName() {
        return UUID.randomUUID().toString();
    }
}
