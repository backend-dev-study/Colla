package kr.kro.colla.user.notice.service;

import kr.kro.colla.user.notice.domain.Notice;
import kr.kro.colla.user.notice.domain.repository.NoticeRepository;
import kr.kro.colla.user.notice.service.dto.CreateNoticeRequest;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
@RequiredArgsConstructor
@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final UserService userService;

    public Notice createNotice(@Valid CreateNoticeRequest createNoticeRequest) {
        User user = userService.findUserById(createNoticeRequest.getTargetUserId());

        Notice notice = Notice.builder()
                .noticeType(createNoticeRequest.getNoticeType())
                .mentionedURL(createNoticeRequest.getMentionedURL())
                .build();

        Notice result = noticeRepository.save(notice);
        user.addNotice(result);

        return result;
    }
}
