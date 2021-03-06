package kr.kro.colla.project.task_status.domain;

import kr.kro.colla.task.task.domain.Task;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "taskStatus", fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    public TaskStatus(String name){
        this.name = name;
    }
}
