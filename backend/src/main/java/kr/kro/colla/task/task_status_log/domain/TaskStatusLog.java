package kr.kro.colla.task.task_status_log.domain;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.task.task.domain.Task;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TaskStatusLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @CreatedDate
    private LocalDate createdAt;

    @Builder
    public TaskStatusLog(String status, Project project, Task task) {
        this.status = status;
        this.project = project;
        this.task = task;
    }

}
