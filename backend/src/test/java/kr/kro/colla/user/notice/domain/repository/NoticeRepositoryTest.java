package kr.kro.colla.user.notice.domain.repository;

import kr.kro.colla.user.notice.domain.Notice;
import kr.kro.colla.user.notice.domain.NoticeType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@ActiveProfiles("test")
@DataJpaTest
class NoticeRepositoryTest {

    @Autowired
    private NoticeRepository noticeRepository;

    @Test
    void 알림_생성에_성공한다(){
        // given
        Notice notice = Notice.builder()
                .noticeType(NoticeType.INVITE_USER)
                .build();

        // when
        Notice result = noticeRepository.save(notice);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getNoticeType()).isEqualTo(NoticeType.INVITE_USER);
        assertThat(result.getIsChecked()).isEqualTo(false);
    }

    @Test
    void 알림_생성에_필요_필드_부족으로_실패한다(){
        // given
        Notice notice = Notice.builder()
                .build();

        // when
        assertThatThrownBy(()->{
            noticeRepository.save(notice);
        }).isInstanceOf(ConstraintViolationException.class);
    }

}