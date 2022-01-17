package kr.kro.colla.user.user.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Getter
public class CreateProjectRequest {

    @NotBlank
    private String name;

    private String description;

    private MultipartFile thumbnail;

    @Builder
    public CreateProjectRequest(String name, String description, MultipartFile thumbnail) {
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

}
