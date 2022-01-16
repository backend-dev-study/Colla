package kr.kro.colla.user.notice.service.dto;

import kr.kro.colla.user.notice.domain.NoticeType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@Builder
public class CreateNoticeRequest {

    @NotNull
    private NoticeType noticeType;

    private String mentionedURL;

}
