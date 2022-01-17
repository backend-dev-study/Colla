package kr.kro.colla.project.project.presentation.dto;

import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user_project.domain.UserProject;
import lombok.Getter;

@Getter
public class ProjectMemberResponse {

    private String name;

    private String avatar;

    private String githubId;

    public ProjectMemberResponse(UserProject userProject){
        User user = userProject.getUser();
        this.name = user.getName();
        this.avatar = user.getAvatar();
        this.githubId = user.getGithubId();
    }
}
