package kr.kro.colla.meeting_place.mentioned_post.domain;

import javax.persistence.*;

@Entity
public class MentionedPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String mentionedUrl;

}
