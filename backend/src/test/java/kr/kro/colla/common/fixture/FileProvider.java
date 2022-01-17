package kr.kro.colla.common.fixture;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class FileProvider {

    public static MockMultipartFile getTestMultipartFile(String fileName) {
        return new MockMultipartFile("image", fileName, "image/png", fileName.getBytes());
    }

    public static String extractImageUrl(MultipartFile file) {
        return String.format("https://colla.s3.com/%s", file.getName());
    }

}
