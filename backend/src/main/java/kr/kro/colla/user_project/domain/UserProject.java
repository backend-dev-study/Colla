package kr.kro.colla.user_project.domain;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.user.user.domain.User;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class UserProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

}
