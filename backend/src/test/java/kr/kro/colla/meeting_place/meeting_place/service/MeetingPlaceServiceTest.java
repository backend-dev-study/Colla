package kr.kro.colla.meeting_place.meeting_place.service;

import kr.kro.colla.common.fixture.MeetingPlaceProvider;
import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.meeting_place.meeting_place.domain.MeetingPlace;
import kr.kro.colla.meeting_place.meeting_place.domain.repository.MeetingPlaceRepository;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.CreateMeetingPlaceRequest;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.MeetingPlaceResponse;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MeetingPlaceServiceTest {

    @Mock
    MeetingPlaceRepository meetingPlaceRepository;

    @Mock
    ProjectService projectService;

    @InjectMocks
    MeetingPlaceService meetingPlaceService;

    @Test
    void 프로젝트의_모임_장소를_생성한다() {
        // given
        Long projectId = 32492L;
        Project project = ProjectProvider.createProject(234912L);
        MeetingPlace meetingPlace = MeetingPlaceProvider.createMeetingPlace();
        CreateMeetingPlaceRequest request = new CreateMeetingPlaceRequest();

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
}