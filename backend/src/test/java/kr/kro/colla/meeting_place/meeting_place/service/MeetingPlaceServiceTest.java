package kr.kro.colla.meeting_place.meeting_place.service;

import kr.kro.colla.common.fixture.MeetingPlaceProvider;
import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.exception.exception.meeting_place.MeetingPlaceNotFoundException;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

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

    @Test
    void 프로젝트의_모임_장소들을_조회한다() {
        // given
        Long projectId = 52514L;
        Project project = ProjectProvider.createProject(3205L);
        List<MeetingPlace> meetingPlaces = List.of(
                MeetingPlaceProvider.createMeetingPlaceWithName("모임 장소 검색시 이미지 조회.."),
                MeetingPlaceProvider.createMeetingPlaceWithName("모임 장소 검색시 현재 좌표 반영.."),
                MeetingPlaceProvider.createMeetingPlaceWithName("모임 장소 저장시 기존 존재 장소 식별..")
        );
        ReflectionTestUtils.setField(project, "meetingPlaces", meetingPlaces);

        given(projectService.findProjectById(projectId))
                .willReturn(project);

        // when
        List<MeetingPlaceResponse> response = meetingPlaceService.getMeetingPlaces(projectId);

        // then
        assertThat(response.size()).isEqualTo(meetingPlaces.size());
        IntStream.range(0, response.size())
                .forEach(i -> assertThat(response.get(i).getName()).isEqualTo(meetingPlaces.get(i).getName()));
    }

    @Test
    void 선택한_모임_장소를_삭제한다() {
        // given
        Long meetingPlaceId = 1L;
        Project project = ProjectProvider.createProject(5L);
        MeetingPlace meetingPlace = MeetingPlaceProvider.createMeetingPlace(project);

        given(meetingPlaceRepository.findById(eq(meetingPlaceId)))
                .willReturn(Optional.of(meetingPlace));
        willDoNothing()
                .given(meetingPlaceRepository)
                .delete(any(MeetingPlace.class));

        // when
        meetingPlaceService.deleteMeetingPlace(meetingPlaceId);

        // then
        verify(meetingPlaceRepository, times(1)).findById(anyLong());
        verify(meetingPlaceRepository, times(1)).delete(any(MeetingPlace.class));
    }

    @Test
    void 삭제하려는_모임_장소가_없을_경우_예외가_발생한다() {
        // given
        Long wrongId = 5L;

        given(meetingPlaceRepository.findById(eq(wrongId)))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> meetingPlaceService.deleteMeetingPlace(wrongId))
                .isInstanceOf(MeetingPlaceNotFoundException.class);
        verify(meetingPlaceRepository, times(1)).findById(anyLong());
        verify(meetingPlaceRepository, never()).delete(any(MeetingPlace.class));
    }

}
