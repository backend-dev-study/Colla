package kr.kro.colla.task.task_status_log.domain;

import kr.kro.colla.project.project.domain.Project;
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

    private Integer count;

    @CreatedDate
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Builder
    public TaskStatusLog(Project project, String status) {
        this.project = project;
        this.status = status;
        this.count = 1;
    }

    public void increaseCount() {
        this.count += 1;
    }

    public void decreaseCount() {
        this.count -= 1;
    }

}
