package kr.kro.colla.user.user.presentation.dto;

import lombok.Getter;

@Getter
public class UpdateUserNameResponse {

    private String name;

    public UpdateUserNameResponse(String name) {
        this.name = name;
    }

}
