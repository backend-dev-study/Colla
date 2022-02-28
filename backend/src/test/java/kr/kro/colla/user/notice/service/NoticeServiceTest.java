package kr.kro.colla.user.notice.service;


import kr.kro.colla.common.fixture.NoticeProvider;
import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.common.fixture.UserProvider;
import kr.kro.colla.exception.exception.notice.NoticeNotFoundException;
import kr.kro.colla.exception.exception.user.UserNotReceiverException;
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

    @InjectMocks
    private NoticeService noticeService;

    @Test
    void 알림_생성에_성공한다() {
        // given
        Long noticeId = 123L, userId = 324L, projectId = 3456L;
        Notice notice = NoticeProvider.createInviteNotice();
        User user = UserProvider.createUser2();
        Project project = ProjectProvider.createProject(345345L);

        ReflectionTestUtils.setField(notice, "id", noticeId);
        ReflectionTestUtils.setField(user, "id", userId);
        ReflectionTestUtils.setField(project, "id", projectId);

        given(noticeRepository.save(any(Notice.class)))
                .willReturn(notice);

        // when
        Notice result = noticeService.createNotice(project, user);

        // then
        assertThat(result.getId()).isEqualTo(noticeId);
        assertThat(result.getIsChecked()).isEqualTo(false);
        assertThat(result.getNoticeType()).isEqualTo(NoticeType.INVITE_USER);
        assertThat(result.getProjectName()).isEqualTo(notice.getProjectName());
        assertThat(user.getNotices().size()).isEqualTo(1);
        verify(noticeRepository, times(1)).save(any(Notice.class));
    }

    @Test
    void 알림_조회에_성공한다() {
        // given
        Long noticeId = 76253L;
        Notice notice = NoticeProvider.createMentionNotice();
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

    @Test
    void 알림_확인에_성공한다() {
        // given
        Long noticeId = 63424L;
        User user = UserProvider.createUser2();
        Notice notice = NoticeProvider.createInviteNotice();
        user.addNotice(notice);

        given(noticeRepository.findById(noticeId))
                .willReturn(Optional.of(notice));

        // when
        noticeService.checkNotice(noticeId, user);

        // then
        assertThat(notice.getIsChecked()).isEqualTo(true);
    }

    @Test
    void 알림의_수신자가_아닐시_알림_확인에_실패한다() {
        // given
        Long noticeId = 63424L;
        User user = UserProvider.createUser2();
        Notice notice = NoticeProvider.createInviteNotice();

        given(noticeRepository.findById(noticeId))
                .willReturn(Optional.of(notice));

        // when, then
        assertThatThrownBy(()->{
            noticeService.checkNotice(noticeId, user);
        }).isInstanceOf(UserNotReceiverException.class);
    }
}
