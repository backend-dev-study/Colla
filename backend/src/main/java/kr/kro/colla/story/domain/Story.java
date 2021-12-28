package kr.kro.colla.story.domain;

import kr.kro.colla.task.task.domain.Task;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String preStories;

    @OneToMany(mappedBy = "story", fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

}
