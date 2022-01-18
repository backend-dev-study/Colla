package kr.kro.colla.user.notice.domain.repository;

import kr.kro.colla.user.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

}
