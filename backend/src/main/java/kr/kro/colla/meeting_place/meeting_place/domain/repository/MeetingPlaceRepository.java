package kr.kro.colla.meeting_place.meeting_place.domain.repository;

import kr.kro.colla.meeting_place.meeting_place.domain.MeetingPlace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingPlaceRepository extends JpaRepository<MeetingPlace, Long> {
}
