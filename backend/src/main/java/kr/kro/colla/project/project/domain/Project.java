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
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column
    private Long managerId;

    @NotNull
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
    public Project(Long managerId, String name, String description){
        this.managerId = managerId;
        this.name = name;
        this.description = description;
    }

    @PrePersist
    private void setDefaultTaskStatus(){
        this.taskStatuses.add(TaskStatus.builder().name("To Do").build());
        this.taskStatuses.add(TaskStatus.builder().name("In Progress").build());
        this.taskStatuses.add(TaskStatus.builder().name("Done").build());
    }

    public void addStatus(TaskStatus taskStatus){
        this.taskStatuses.add(taskStatus);
    }
}
