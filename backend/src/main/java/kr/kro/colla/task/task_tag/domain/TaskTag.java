package kr.kro.colla.task.task_tag.domain;

import kr.kro.colla.task.tag.domain.Tag;
import kr.kro.colla.task.task.domain.Task;

import javax.persistence.*;

@Entity
public class TaskTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

}
