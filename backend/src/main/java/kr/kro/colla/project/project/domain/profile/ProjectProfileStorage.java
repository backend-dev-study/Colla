package kr.kro.colla.project.project.domain.profile;

import org.springframework.web.multipart.MultipartFile;

public interface ProjectProfileStorage {

    String upload(MultipartFile file);
}
