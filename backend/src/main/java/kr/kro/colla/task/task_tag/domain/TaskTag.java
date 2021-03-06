package kr.kro.colla.task.task_tag.domain;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.task.tag.domain.Tag;
import kr.kro.colla.task.task.domain.Task;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class TaskTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public TaskTag(Project project, Tag tag) {
        this.project = project;
        this.tag = tag;
    }

    public TaskTag(Task task, Tag tag) {
        this.task = task;
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaskTag taskTag = (TaskTag) o;
        return Objects.equals(task, taskTag.task) && Objects.equals(tag, taskTag.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(task, tag);
    }
}
