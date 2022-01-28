package kr.kro.colla.project.project.presentation.dto;

import kr.kro.colla.user.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProjectMemberResponse {

    private Long id;

    private String name;

    private String avatar;

    private String githubId;

    public ProjectMemberResponse(User user){
        this.id = user.getId();
        this.name = user.getName();
        this.avatar = user.getAvatar();
        this.githubId = user.getGithubId();
    }
}
