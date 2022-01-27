package kr.kro.colla.user.user.presentation.dto;

import kr.kro.colla.user.notice.domain.Notice;
import kr.kro.colla.user.notice.domain.NoticeType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserNoticeResponse {
    private Long id;

    private NoticeType noticeType;

    private String mentionedURL;

    private Boolean isChecked;

    public UserNoticeResponse(Notice notice){
        this.id = notice.getId();
        this.noticeType = notice.getNoticeType();
        this.mentionedURL = notice.getMentionedURL();
        this.isChecked = notice.getIsChecked();
    }
}
