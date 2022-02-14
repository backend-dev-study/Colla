package kr.kro.colla.project.project.domain;

import kr.kro.colla.story.domain.Story;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.task.task_tag.domain.TaskTag;
import kr.kro.colla.user_project.domain.UserProject;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column
    private String thumbnail;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<UserProject> members = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    @JoinColumn(name = "project_id")
    private List<TaskStatus> taskStatuses = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<Story> stories = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    private List<TaskTag> taskTags = new ArrayList<>();

    @Builder
    public Project(Long managerId, String name, String description, String thumbnail){
        this.managerId = managerId;
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    @PrePersist
    private void setDefaultTaskStatus(){
        this.taskStatuses.add(new TaskStatus("To Do"));
        this.taskStatuses.add(new TaskStatus("In Progress"));
        this.taskStatuses.add(new TaskStatus("Done"));
    }

    public void addStatus(TaskStatus taskStatus){
        this.taskStatuses.add(taskStatus);
    }

    public void removeStatus(TaskStatus taskStatus) { this.taskStatuses.remove(taskStatus); }
}
