package kr.kro.colla.user.notice.domain;

import kr.kro.colla.user.notice.domain.converter.BooleanToIntegerConverter;
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
    private String mentionedUrl;

    @NotNull
    @Convert(converter = BooleanToIntegerConverter.class)
    @Column
    private Boolean isChecked;

    @Builder
    public Notice(NoticeType noticeType, String mentionedUrl){
        this.noticeType = noticeType;
        this.isChecked = false;
        this.mentionedUrl = mentionedUrl;
    }
}
