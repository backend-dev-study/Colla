package kr.kro.colla.project.task_status.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class TaskStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

}
