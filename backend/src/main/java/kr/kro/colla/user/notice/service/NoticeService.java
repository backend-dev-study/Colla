package kr.kro.colla.user.notice.service;

import kr.kro.colla.user.notice.domain.Notice;
import kr.kro.colla.user.notice.domain.repository.NoticeRepository;
import kr.kro.colla.user.notice.service.dto.CreateNoticeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@RequiredArgsConstructor
@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;


    public Notice createNotice(@Valid CreateNoticeRequest createNoticeRequest) {
        Notice notice = Notice.builder()
                .noticeType(createNoticeRequest.getNoticeType())
                .mentionedURL(createNoticeRequest.getMentionedURL())
                .build();

        return noticeRepository.save(notice);
    }
}
