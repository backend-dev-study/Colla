package kr.kro.colla.meeting_place.meeting_place.domain;

import kr.kro.colla.meeting_place.mentioned_post.domain.MentionedPost;
import kr.kro.colla.project.project.domain.Project;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column
    private String name;

    @Column
    private String image;

    @NotNull
    @Column
    private Double longitude;

    @NotNull
    @Column
    private Double latitude;

    @NotNull
    @Column
    private String address;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_place_id")
    private List<MentionedPost> mentionedPosts = new ArrayList<>();

    @Builder
    public MeetingPlace(String name, String image, Double longitude, Double latitude, String address, Project project) {
        this.name = name;
        this.image = image;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.project = project;
    }
}
