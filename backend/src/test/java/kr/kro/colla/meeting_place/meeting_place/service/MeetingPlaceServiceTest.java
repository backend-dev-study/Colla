package kr.kro.colla.meeting_place.meeting_place.service;

import kr.kro.colla.common.fixture.MeetingPlaceProvider;
import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.meeting_place.meeting_place.domain.MeetingPlace;
import kr.kro.colla.meeting_place.meeting_place.domain.repository.MeetingPlaceRepository;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.CreateMeetingPlaceRequest;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
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

}
