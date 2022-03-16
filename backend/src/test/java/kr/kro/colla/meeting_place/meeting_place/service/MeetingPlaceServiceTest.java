package kr.kro.colla.meeting_place.meeting_place.service;

import kr.kro.colla.common.fixture.MeetingPlaceProvider;
import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.meeting_place.meeting_place.domain.MeetingPlace;
import kr.kro.colla.meeting_place.meeting_place.domain.repository.MeetingPlaceRepository;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.CreateMeetingPlaceRequest;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.MeetingPlaceResponse;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.SearchByMapBoundaryRequest;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MeetingPlaceServiceTest {

    @Mock
    private MeetingPlaceRepository meetingPlaceRepository;

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private MeetingPlaceService meetingPlaceService;

    @Test
    void 프로젝트의_모임_장소를_생성한다() {
        // given
        Long projectId = 32492L;
        Project project = ProjectProvider.createProject(234912L);
        MeetingPlace meetingPlace = MeetingPlaceProvider.createMeetingPlace(project);
        CreateMeetingPlaceRequest request = new CreateMeetingPlaceRequest("이번 회의 장소", null, 132.234, 23.234, "서울특별시 강남구 ~~로 ~~번길");

        given(projectService.findProjectById(projectId))
                .willReturn(project);
        given(meetingPlaceRepository.save(any(MeetingPlace.class)))
                .willReturn(meetingPlace);

        // when
        MeetingPlace result = meetingPlaceService.createMeetingPlace(projectId, request);

        // then
        assertThat(result.getName()).isEqualTo(meetingPlace.getName());
        assertThat(result.getLongitude()).isEqualTo(meetingPlace.getLongitude());
        assertThat(result.getAddress()).isEqualTo(meetingPlace.getAddress());
        verify(meetingPlaceRepository, times(1)).save(any(MeetingPlace.class));
    }

    @Test
    void 지도_바운더리에_해당하는_모임_장소들을_조회한다() {
        // given
        Long projectId = 5L;
        Project project = ProjectProvider.createProject(3L);
        SearchByMapBoundaryRequest request = new SearchByMapBoundaryRequest(127.022, 127.918, 36.383, 37.489);
        List<MeetingPlace> meetingPlaceList = List.of(
                MeetingPlaceProvider.createMeetingPlaceWithCoordinate(project, 127.523, 37.024),
                MeetingPlaceProvider.createMeetingPlaceWithCoordinate(project, 127.218, 36.938)
        );

        given(projectService.findProjectById(eq(projectId)))
                .willReturn(project);
        given(meetingPlaceRepository.findMeetingPlacesByBoundary(any(Project.class), any(SearchByMapBoundaryRequest.class)))
                .willReturn(meetingPlaceList);

        // when
        List<MeetingPlaceResponse> result = meetingPlaceService.getSpecificAreaMeetingPlace(projectId, request);

        // then
        assertThat(result).hasSize(2);
        result.forEach(meetingPlace -> {
            assertThat(meetingPlace.getLongitude())
                    .isGreaterThanOrEqualTo(request.getMinLng())
                    .isLessThanOrEqualTo(request.getMaxLng());
            assertThat(meetingPlace.getLatitude())
                    .isGreaterThanOrEqualTo(request.getMinLat())
                    .isLessThanOrEqualTo(request.getMaxLat());
        });
        verify(meetingPlaceRepository, times(1)).findMeetingPlacesByBoundary(any(Project.class), any(SearchByMapBoundaryRequest.class));
    }

}
