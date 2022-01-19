package kr.kro.colla.user.user.presentation.dto;

import kr.kro.colla.user.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserProfileResponse {

    private String displayName;

    private String githubId;

    private String avatar;

    public UserProfileResponse(User user) {
        this.displayName = user.getName();
        this.githubId = user.getGithubId();
        this.avatar = user.getAvatar();
    }

}
