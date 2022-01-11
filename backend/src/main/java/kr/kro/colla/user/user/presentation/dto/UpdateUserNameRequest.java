package kr.kro.colla.user.user.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UpdateUserNameRequest {

    private String name;

    public UpdateUserNameRequest(String name) {
        this.name = name;
    }

}
