package kr.kro.colla.task.tag.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

}
