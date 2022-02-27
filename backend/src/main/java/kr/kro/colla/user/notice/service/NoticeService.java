package kr.kro.colla.user.notice.service;

import kr.kro.colla.exception.exception.notice.NoticeNotFoundException;
import kr.kro.colla.exception.exception.user.UserNotReceiverException;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.presentation.dto.ProjectMemberDecision;
import kr.kro.colla.project.project.presentation.dto.ProjectMemberResponse;
import kr.kro.colla.user.notice.domain.Notice;
import kr.kro.colla.user.notice.domain.NoticeType;
import kr.kro.colla.user.notice.domain.repository.NoticeRepository;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.service.UserService;
import kr.kro.colla.user_project.domain.UserProject;
import kr.kro.colla.user_project.service.UserProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public Notice createNotice(Project project, User receiver) {
        Notice notice = Notice.builder()
                .noticeType(NoticeType.INVITE_USER)
                .projectId(project.getId())
                .projectName(project.getName())
                .build();

        Notice result = noticeRepository.save(notice);
        receiver.addNotice(result);

        return result;
    }

    public void checkNotice(Long noticeId, User receiver) {
        Notice notice = findById(noticeId);

        if (!receiver.getNotices().contains(notice)){
            throw new UserNotReceiverException();
        }
        notice.check();
    }

    public Notice findById(Long id) {
        return noticeRepository.findById(id)
                .orElseThrow(NoticeNotFoundException::new);
    }
}
