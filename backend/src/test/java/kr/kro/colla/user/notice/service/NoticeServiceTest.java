package kr.kro.colla.user.notice.service;


import kr.kro.colla.exception.exception.user.UserNotFoundException;
import kr.kro.colla.user.notice.domain.Notice;
import kr.kro.colla.user.notice.domain.NoticeType;
import kr.kro.colla.user.notice.domain.repository.NoticeRepository;
import kr.kro.colla.user.notice.service.dto.CreateNoticeRequest;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.service.UserService;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.List;
import java.util.Set;

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

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void 알림_생성에_성공한다() {
        // given
        String mentionURL = "mention";
        Long id = 123L, userId = 324L;
        Notice notice = Notice.builder()
                .noticeType(NoticeType.INVITE_USER)
                .mentionedURL(mentionURL)
                .build();
        User user = User.builder()
                .name("binimini")
                .githubId("binimini")
                .avatar("github_content")
                .build();
        ReflectionTestUtils.setField(notice, "id", id);
        CreateNoticeRequest createNoticeRequest = CreateNoticeRequest.builder()
                .noticeType(NoticeType.INVITE_USER)
                .mentionedURL(mentionURL)
                .targetUserId(userId)
                .build();

        given(noticeRepository.save(any(Notice.class)))
                .willReturn(notice);
        given(userService.findUserById(userId))
                .willReturn(user);

        // when
        Notice result = noticeService.createNotice(createNoticeRequest);

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getIsChecked()).isEqualTo(false);
        assertThat(result.getNoticeType()).isEqualTo(NoticeType.INVITE_USER);
        assertThat(result.getMentionedURL()).isEqualTo(mentionURL);
        assertThat(user.getNotices().size()).isEqualTo(1);
        verify(noticeRepository, times(1)).save(any(Notice.class));
    }

    @Test
    void 알림_생성시_필드_검증에_실패한다(){
        // given
        String mentionURL = "mention";
        Long userId = 234L;
        CreateNoticeRequest createNoticeRequest1 = CreateNoticeRequest.builder()
                .mentionedURL(mentionURL)
                .targetUserId(userId)
                .build();

        CreateNoticeRequest createNoticeRequest2 = CreateNoticeRequest.builder()
                .noticeType(NoticeType.INVITE_USER)
                .mentionedURL(mentionURL)
                .build();

        // when
        Set<ConstraintViolation<CreateNoticeRequest>> violations1 = validator.validate(createNoticeRequest1);
        Set<ConstraintViolation<CreateNoticeRequest>> violations2 = validator.validate(createNoticeRequest2);

        // then
        assertThat(violations1.size()).isEqualTo(1);
        assertThat(violations1.toArray()).extracting("propertyPath").toString().contains("noticeType");

        assertThat(violations2.size()).isEqualTo(1);
        assertThat(violations1.toArray()).extracting("propertyPath").toString().contains("targetUserId");
    }

    @Test
    void 알림_생성시_존재하지_않는_대상_사용자로_실패한다(){
        // given
        String mentionURL = "mention";
        Long userId = 234L;
        CreateNoticeRequest createNoticeRequest = CreateNoticeRequest.builder()
                .mentionedURL(mentionURL)
                .targetUserId(userId)
                .build();

        given(userService.findUserById(userId))
                .willThrow(new UserNotFoundException());

        // when
        assertThatThrownBy(()->{
            noticeService.createNotice(createNoticeRequest);
        }).isInstanceOf(UserNotFoundException.class);

    }
}
