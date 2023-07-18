package teameight.gg.domain.post.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import teameight.gg.global.exception.UploadException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static teameight.gg.global.stringCode.ErrorCodeEnum.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    public String upload(MultipartFile multipartFile, String dirName) {
        if (multipartFile == null || multipartFile.isEmpty()) return "";

        try {
            File uploadFile = convert(multipartFile)  // 파일 생성
                    .orElseThrow(() -> new UploadException(FILE_INVALID));
            return upload(uploadFile, dirName);
        } catch (IOException e) {
            throw new UploadException(UPLOAD_FAIL, e);
        }
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    // 1. 로컬에 파일생성
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            } catch (IOException e) {
                throw new UploadException(UPLOAD_FAIL, e);
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    // 2. S3에 파일업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        log.info("File Upload : " + fileName);
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 3. 로컬에 생성된 파일삭제
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("로컬 파일 삭제 성공");
            return;
        }
        log.info("로컬 파일 삭제 실패");
    }

}
