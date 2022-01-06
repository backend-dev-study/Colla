package kr.kro.colla.auth.domain;

import lombok.Getter;

@Getter
public class LoginUser {

    private Long id;

    public LoginUser(Long id) {
        this.id = id;
    }

}
