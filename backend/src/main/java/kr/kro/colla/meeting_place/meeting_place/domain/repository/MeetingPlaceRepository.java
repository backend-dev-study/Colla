package kr.kro.colla.meeting_place.meeting_place.domain.repository;

import kr.kro.colla.meeting_place.meeting_place.domain.MeetingPlace;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.SearchByMapBoundaryRequest;
import kr.kro.colla.project.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeetingPlaceRepository extends JpaRepository<MeetingPlace, Long> {

    @Query("select m from MeetingPlace m " +
            "where m.project = :project " +
            "and :#{#boundary.minLongitude} <= m.longitude and m.longitude <= :#{#boundary.maxLongitude} " +
            "and :#{#boundary.minLatitude} <= m.latitude and m.latitude <= :#{#boundary.maxLatitude}")
    List<MeetingPlace> findMeetingPlacesByBoundary(@Param("project") Project project, @Param("boundary")SearchByMapBoundaryRequest request);

}
