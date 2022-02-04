package kr.kro.colla.user.notice.service;

import kr.kro.colla.exception.exception.notice.NoticeNotFoundException;
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

    private final UserService userService;
    private final UserProjectService userProjectService;
    private final NoticeRepository noticeRepository;

    public Notice createNotice(Project project, String memberGithubId) {
        User user = userService.findByGithubId(memberGithubId);

        Notice notice = Notice.builder()
                .noticeType(NoticeType.INVITE_USER)
                .projectId(project.getId())
                .projectName(project.getName())
                .build();

        Notice result = noticeRepository.save(notice);
        user.addNotice(result);

        return result;
    }

    public ProjectMemberResponse decideInvitation(Long userId, Project project, ProjectMemberDecision projectMemberDecision) {
        User user = userService.findUserById(userId);
        Notice notice = findById(projectMemberDecision.getNoticeId());
        notice.check();

        if (projectMemberDecision.isAccept()) {
            UserProject userProject = userProjectService.joinProject(user, project);
            return new ProjectMemberResponse(userProject.getUser());
        }

        return null;
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
