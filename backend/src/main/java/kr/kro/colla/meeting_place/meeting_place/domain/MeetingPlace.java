package kr.kro.colla.meeting_place.meeting_place.domain;

import kr.kro.colla.meeting_place.mentioned_post.domain.MentionedPost;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class MeetingPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String image;

    @Column
    private String review;

    @Column
    private String longitude;

    @Column
    private String latitude;

    @Column
    private String address;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_place_id")
    private List<MentionedPost> mentionedPosts = new ArrayList<>();

}
