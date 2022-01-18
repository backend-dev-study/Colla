package kr.kro.colla.user.notice.service.dto;

import kr.kro.colla.user.notice.domain.NoticeType;
import kr.kro.colla.user.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
public class CreateNoticeRequest {

    @NotNull
    private NoticeType noticeType;

    private String mentionedURL;

    @NotNull
    private User receiver;
}
