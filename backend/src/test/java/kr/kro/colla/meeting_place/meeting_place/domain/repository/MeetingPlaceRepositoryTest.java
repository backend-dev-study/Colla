package kr.kro.colla.meeting_place.meeting_place.domain.repository;

import kr.kro.colla.common.fixture.MeetingPlaceProvider;
import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.meeting_place.meeting_place.domain.MeetingPlace;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.SearchByMapBoundaryRequest;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class MeetingPlaceRepositoryTest {

    @Autowired
    private MeetingPlaceRepository meetingPlaceRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void 프로젝트의_모임_장소를_생성한다() {
        // given
        Project project = projectRepository.save(ProjectProvider.createProject(324091L));
        MeetingPlace meetingPlace = MeetingPlaceProvider.createMeetingPlace(project);

        // when
        MeetingPlace result = meetingPlaceRepository.save(meetingPlace);

        // then
        assertThat(result.getName()).isEqualTo(meetingPlace.getName());
        assertThat(result.getLongitude()).isEqualTo(meetingPlace.getLongitude());
        assertThat(result.getLatitude()).isEqualTo(meetingPlace.getLatitude());
        assertThat(result.getAddress()).isEqualTo(meetingPlace.getAddress());
        assertThat(result.getProject().getId()).isEqualTo(project.getId());
    }

    @Test
    void 모임_장소의_필수_필드를_부족시_생성할_수_없다() {
        // given
        MeetingPlace meetingPlace = MeetingPlace.builder()
                .build();

        // when, then
        assertThatThrownBy(() -> {
            meetingPlaceRepository.save(meetingPlace);
        }).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void 지도_바운더리에_해당하는_모임_장소들을_조회한다() {
        // given
        Project project = projectRepository.save(ProjectProvider.createProject(1L));
        meetingPlaceRepository.saveAll(List.of(
                MeetingPlaceProvider.createMeetingPlaceWithCoordinate(project, 127.031, 37.491),
                MeetingPlaceProvider.createMeetingPlaceWithCoordinate(project, 128.521, 36.723),
                MeetingPlaceProvider.createMeetingPlaceWithCoordinate(project, 127.032, 37.487),
                MeetingPlaceProvider.createMeetingPlaceWithCoordinate(project, 126.684, 35.265)
        ));
        SearchByMapBoundaryRequest request = new SearchByMapBoundaryRequest(127.022, 127.918, 37.383, 37.489);

        // when
        List<MeetingPlace> meetingPlaceList = meetingPlaceRepository.findMeetingPlacesByBoundary(project, request);

        // then
        assertThat(meetingPlaceList).hasSize(1);
        assertThat(meetingPlaceList.get(0).getLongitude())
                .isGreaterThanOrEqualTo(request.getMinLng())
                .isLessThanOrEqualTo(request.getMaxLng());
        assertThat(meetingPlaceList.get(0).getLatitude())
                .isGreaterThanOrEqualTo(request.getMinLat())
                .isLessThanOrEqualTo(request.getMaxLat());
    }
}
