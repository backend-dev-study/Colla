package kr.kro.colla.project.project.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateRequest {
    @NonNull
    private String name;
    private String description;
}
