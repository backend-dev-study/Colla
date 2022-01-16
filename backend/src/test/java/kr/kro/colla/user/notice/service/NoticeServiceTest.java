package kr.kro.colla.user.notice.service;


import kr.kro.colla.user.notice.domain.Notice;
import kr.kro.colla.user.notice.domain.NoticeType;
import kr.kro.colla.user.notice.domain.repository.NoticeRepository;
import kr.kro.colla.user.notice.service.dto.CreateNoticeRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ConstraintViolationException;

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
        String mentionURL = "mention";
        Long id = 123L;
        Notice notice = Notice.builder()
                .noticeType(NoticeType.INVITE_USER)
                .mentionedURL(mentionURL)
                .build();
        ReflectionTestUtils.setField(notice, "id", id);
        CreateNoticeRequest createNoticeRequest = CreateNoticeRequest.builder()
                .noticeType(NoticeType.INVITE_USER)
                .mentionedURL(mentionURL)
                .build();

        given(noticeRepository.save(any(Notice.class))).willReturn(notice);

        // when
        Notice result = noticeService.createNotice(createNoticeRequest);

        // then
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getIsChecked()).isEqualTo(false);
        assertThat(result.getNoticeType()).isEqualTo(NoticeType.INVITE_USER);
        assertThat(result.getMentionedURL()).isEqualTo(mentionURL);
        verify(noticeRepository, times(1)).save(any(Notice.class));
    }

}
