package kr.kro.colla.user.domain;

import kr.kro.colla.notice.domain.Notice;
import kr.kro.colla.user_project.domain.UserProject;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String githubId;

    @Column
    private String name;

    @Column
    private String avatar;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserProject> projects = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private List<Notice> notices = new ArrayList<>();

    @Builder
    public User(String name, String githubId, String avatar) {
        this.name = name;
        this.githubId = githubId;
        this.avatar = avatar;
    }

}
