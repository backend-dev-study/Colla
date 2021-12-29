package kr.kro.colla.project.project.domain;

import kr.kro.colla.story.domain.Story;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.user_project.domain.UserProject;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long managerId;

    @Column
    private String name;

    @Column
    private String description;


    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<UserProject> members = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    @JoinColumn(name = "project_id")
    private List<TaskStatus> taskStatuses = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private List<Story> stories = new ArrayList<>();

    @Builder
    public Project(@NonNull Long managerId, @NonNull String name, String description){
        this.managerId = managerId;
        this.name = name;
        this.description = description;
    }
}
