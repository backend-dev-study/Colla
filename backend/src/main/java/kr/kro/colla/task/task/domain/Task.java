package kr.kro.colla.task.task.domain;

import kr.kro.colla.comment.domain.Comment;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.task.history.domain.History;
import kr.kro.colla.story.domain.Story;
import kr.kro.colla.task.task.presentation.dto.UpdateTaskRequest;
import kr.kro.colla.task.task_tag.domain.TaskTag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(EntityListeners.class)
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long managerId;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String images;

    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    private String preTasks;

    @Column
    private Integer priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "task_status_id")
    private TaskStatus taskStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id")
    private Story story;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(
            mappedBy = "task",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<TaskTag> taskTags = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private List<History> histories = new ArrayList<>();

    @Builder
    public Task(String title, Long managerId, String description, Integer priority, Project project, TaskStatus taskStatus, Story story, String preTasks) {
        this.title = title;
        this.managerId = managerId;
        this.description = description;
        this.priority = priority;
        this.project = project;
        this.taskStatus = taskStatus;
        this.story = story;
        this.preTasks = preTasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void updateContents(UpdateTaskRequest updateTaskRequest) {
        if (updateTaskRequest.getManagerId().matches("[0-9]+")) {
            this.managerId = Long.parseLong(updateTaskRequest.getManagerId());
        }
        this.title = updateTaskRequest.getTitle();
        this.description = updateTaskRequest.getDescription();
        this.priority = updateTaskRequest.getPriority();
        this.preTasks = updateTaskRequest.getPreTasks();
    }

    public void updateStory(Story story) {
        this.story = story;
    }

    public void updateTags(List<TaskTag> updateTaskTags) {
        this.taskTags.removeIf(taskTag -> !updateTaskTags.contains(taskTag));
        this.taskTags.addAll(updateTaskTags.stream()
                .filter(taskTag -> !this.taskTags.contains(taskTag))
                .collect(Collectors.toList()));
    }

}
