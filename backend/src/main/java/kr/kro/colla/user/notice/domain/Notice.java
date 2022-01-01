package kr.kro.colla.user.notice.domain;

import kr.kro.colla.user.notice.domain.converter.BooleanToIntegerConverter;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.ORDINAL)
    private NoticeType noticeType;

    @Column
    private String mentionedUrl;

    @Convert(converter = BooleanToIntegerConverter.class)
    @Column
    private boolean isChecked;

}
