package kr.kro.colla.story.domain;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.story.presentation.dto.UpdateStoryPeriodRequest;
import kr.kro.colla.task.task.domain.Task;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column
    private LocalDate startAt;

    @Column
    private LocalDate endAt;

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

    public void updatePeriod(UpdateStoryPeriodRequest updateStoryPeriodRequest) {
        this.startAt = updateStoryPeriodRequest.getStartAt();
        this.endAt = updateStoryPeriodRequest.getEndAt();
    }

}
