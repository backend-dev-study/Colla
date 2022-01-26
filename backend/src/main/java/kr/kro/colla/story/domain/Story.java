package kr.kro.colla.story.domain;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.task.task.domain.Task;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String preStories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "story", fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    @Builder
    public Story(String title, String preStories, Project project) {
        this.title = title;
        this.preStories = preStories;
        this.project = project;
    }

}
