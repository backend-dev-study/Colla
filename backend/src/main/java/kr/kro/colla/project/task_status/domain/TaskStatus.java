package kr.kro.colla.project.task_status.domain;

import kr.kro.colla.task.task.domain.Task;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class TaskStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private List<Task> tasks = new ArrayList<>();

    @Builder
    public TaskStatus(String name){
        this.name = name;
    }
}
