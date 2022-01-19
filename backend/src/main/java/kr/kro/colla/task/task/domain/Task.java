package kr.kro.colla.task.task.domain;

import kr.kro.colla.comment.domain.Comment;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.task.history.domain.History;
import kr.kro.colla.story.domain.Story;
import kr.kro.colla.task.task_tag.domain.TaskTag;
import org.springframework.data.annotation.CreatedDate;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@EntityListeners(EntityListeners.class)
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String images;

    @Column
    private String managerName;

    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    private String preTasks;

    @Column
    private Integer priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_id")
    private Story story;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
    private List<TaskTag> taskTags = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private List<History> histories = new ArrayList<>();

}
