package kr.kro.colla.user.notice.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@NoArgsConstructor(access=AccessLevel.PROTECTED)
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private NoticeType noticeType;

    @NotNull
    @Column
    private Boolean isChecked;

    @Column
    private String mentionedURL;

    @Column
    private Long projectId;

    @Column
    private String projectName;

    @Builder
    public Notice(NoticeType noticeType, String mentionedURL, Long projectId, String projectName){
        this.isChecked = false;
        this.noticeType = noticeType;
        this.mentionedURL = mentionedURL;
        this.projectId = projectId;
        this.projectName = projectName;
    }

    public void check() { this.isChecked = true; }
}
