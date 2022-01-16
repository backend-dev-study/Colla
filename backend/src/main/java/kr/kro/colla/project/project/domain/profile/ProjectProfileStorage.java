package kr.kro.colla.project.project.domain.profile;

import org.springframework.web.multipart.MultipartFile;

public interface ProjectProfileStorage {

    public String upload(MultipartFile file);
}
