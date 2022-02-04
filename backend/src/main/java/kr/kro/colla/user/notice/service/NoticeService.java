package kr.kro.colla.user.notice.service;

import kr.kro.colla.exception.exception.notice.NoticeBadRequestException;
import kr.kro.colla.exception.exception.notice.NoticeNotFoundException;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.user.notice.domain.Notice;
import kr.kro.colla.user.notice.domain.NoticeType;
import kr.kro.colla.user.notice.domain.repository.NoticeRepository;
import kr.kro.colla.user.notice.service.dto.CreateNoticeRequest;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final UserService userService;

    public Notice createNotice(CreateNoticeRequest createNoticeRequest) {
        User user = userService.findUserById(createNoticeRequest.getReceiverId());

        Notice notice = Notice.builder()
                .noticeType(createNoticeRequest.getNoticeType())
                .mentionedURL(createNoticeRequest.getMentionedURL())
                .projectId(createNoticeRequest.getProjectId())
                .projectName(createNoticeRequest.getProjectName())
                .build();

        Notice result = noticeRepository.save(notice);
        user.addNotice(result);

        return result;
    }

    public Notice checkNotice(Long id) {
        Notice notice = findById(id);
        notice.check();
        return notice;
    }

    public Notice findById(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(NoticeNotFoundException::new);
    }
}
