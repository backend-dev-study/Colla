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
    private final ProjectService projectService;

    public Notice createNotice(CreateNoticeRequest createNoticeRequest) {
        User user = userService.findUserById(createNoticeRequest.getReceiverId());

        Notice notice;
        switch (createNoticeRequest.getNoticeType()){
            case INVITE_USER:
                Project project = projectService.findProjectById(createNoticeRequest.getProjectId());
                notice = Notice.builder()
                        .noticeType(NoticeType.INVITE_USER)
                        .projectId(project.getId())
                        .projectName(project.getName())
                        .build();
                break;
            case MENTION_USER:
                notice = Notice.builder()
                        .noticeType(NoticeType.MENTION_USER)
                        .mentionedURL(createNoticeRequest.getMentionedURL())
                        .build();
                break;
            default:
                throw new NoticeBadRequestException();
        }

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
