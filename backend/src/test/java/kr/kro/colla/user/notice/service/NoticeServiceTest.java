package kr.kro.colla.user.notice.service;


import kr.kro.colla.exception.exception.notice.NoticeNotFoundException;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.user.notice.domain.Notice;
import kr.kro.colla.user.notice.domain.NoticeType;
import kr.kro.colla.user.notice.domain.repository.NoticeRepository;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NoticeServiceTest {
    @Mock
    private NoticeRepository noticeRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private NoticeService noticeService;

    @Test
    void 알림_생성에_성공한다() {
        // given
        String mentionURL = "mention_url", projectName = "project_to_invite";
        Long id = 123L, userId = 324L, projectId = 3456L;
        Notice notice = Notice.builder()
                .noticeType(NoticeType.INVITE_USER)
                .projectName(projectName)
                .projectId(projectId)
                .mentionedURL(mentionURL)
                .build();
        User user = User.builder()
                .name("binimini")
                .githubId("binimini")
                .avatar("github_content")
                .build();
        Project project = Project.builder()
                .name("project name")
                .managerId(5L)
                .build();
        ReflectionTestUtils.setField(notice, "id", id);
        ReflectionTestUtils.setField(user, "id", userId);
        ReflectionTestUtils.setField(project, "id", 1L);


        given(userService.findByGithubId(user.getGithubId()))
                .willReturn(user);
        given(noticeRepository.save(any(Notice.class)))
                .willReturn(notice);

        // when
        Notice result = noticeService.createNotice(project, user.getGithubId());

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getIsChecked()).isEqualTo(false);
        assertThat(result.getNoticeType()).isEqualTo(NoticeType.INVITE_USER);
        assertThat(result.getMentionedURL()).isEqualTo(mentionURL);
        assertThat(result.getProjectId()).isEqualTo(projectId);
        assertThat(result.getProjectName()).isEqualTo(projectName);
        assertThat(user.getNotices().size()).isEqualTo(1);
        verify(noticeRepository, times(1)).save(any(Notice.class));
    }

    @Test
    void 알림_조회에_성공한다() {
        // given
        Long noticeId = 76253L;
        String mention = "path/asdfasf/mention/zxcvvv";
        Notice notice = Notice.builder()
                        .noticeType(NoticeType.MENTION_USER)
                        .mentionedURL(mention)
                        .build();
        ReflectionTestUtils.setField(notice, "id", noticeId);

        given(noticeRepository.findById(noticeId))
                .willReturn(Optional.of(notice));
        // when
        Notice result = noticeService.findById(noticeId);

        // then
        assertThat(result.getId()).isEqualTo(notice.getId());
        assertThat(result.getNoticeType()).isEqualTo(notice.getNoticeType());
        assertThat(result.getMentionedURL()).isEqualTo(notice.getMentionedURL());
        assertThat(result.getProjectId()).isEqualTo(notice.getProjectId());
        verify(noticeRepository, times(1)).findById(noticeId);
    }

    @Test
    void 존재하지_않는_알림_조회시_예외가_발생한다() {
        // given
        Long notFoundNoticeId = 122345L;

        given(noticeRepository.findById(notFoundNoticeId))
                .willThrow(NoticeNotFoundException.class);
        // when
        assertThatThrownBy(() -> {
            noticeService.findById(notFoundNoticeId);
        }).isInstanceOf(NoticeNotFoundException.class);

    }
}
