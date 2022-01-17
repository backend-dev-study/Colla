package kr.kro.colla.project.project.infrastructure;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import kr.kro.colla.exception.exception.user.FileUploadException;
import kr.kro.colla.project.project.domain.profile.ProjectProfileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class S3ProjectProfileStorage implements ProjectProfileStorage {

    private final AmazonS3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String upload(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();

            s3Client.putObject(new PutObjectRequest(bucket, fileName, file.getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return s3Client.getUrl(bucket, fileName).toString();
        } catch (IOException e) {
            throw new FileUploadException();
        }
    }
}
