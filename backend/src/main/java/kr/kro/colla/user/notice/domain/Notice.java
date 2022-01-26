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

    @Column
    private String mentionedURL;

    @NotNull
    @Column
    private Boolean isChecked;

    @Builder
    public Notice(NoticeType noticeType, String mentionedURL){
        this.isChecked = false;
        this.noticeType = noticeType;
        this.mentionedURL = mentionedURL;
    }

    public void check() { this.isChecked = true; }
}
