package kr.kro.colla.meeting_place.meeting_place.presentation;

import kr.kro.colla.common.ControllerTest;
import kr.kro.colla.common.fixture.MeetingPlaceProvider;
import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.exception.exception.meeting_place.MeetingPlaceNotFoundException;
import kr.kro.colla.meeting_place.meeting_place.domain.MeetingPlace;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.CreateMeetingPlaceRequest;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.MeetingPlaceResponse;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.SearchByMapBoundaryRequest;
import kr.kro.colla.project.project.domain.Project;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.Cookie;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeetingPlaceController.class)
class MeetingPlaceControllerTest extends ControllerTest {

    @Test
    void 프로젝트의_모임_장소를_추가한다() throws Exception {
        // given
        Long projectId = 82425L, meetingPlaceId = 234241L;
        CreateMeetingPlaceRequest request = new CreateMeetingPlaceRequest("meeting place name", "preview image", 23.23, 3.5, "address to go!", "category");
        MeetingPlace meetingPlace = MeetingPlaceProvider.createMeetingPlace(ProjectProvider.createProject(1L));
        ReflectionTestUtils.setField(meetingPlace, "id", meetingPlaceId);

        given(meetingPlaceService.createMeetingPlace(eq(projectId), any(CreateMeetingPlaceRequest.class)))
                .willReturn(meetingPlace);
        
        // when
        ResultActions perform = mockMvc.perform(post("/projects/" + projectId + "/meeting-places")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));
        
        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(meetingPlace.getId()))
                .andExpect(jsonPath("$.name").value(meetingPlace.getName()))
                .andExpect(jsonPath("$.longitude").value(meetingPlace.getLongitude()))
                .andExpect(jsonPath("$.latitude").value(meetingPlace.getLatitude()))
                .andExpect(jsonPath("$.address").value(meetingPlace.getAddress()));
        verify(meetingPlaceService, times(1)).createMeetingPlace(eq(projectId), any(CreateMeetingPlaceRequest.class));
    }

    @Test
    void 모임_장소의_정보가_부족할시_추가할_수_없다() throws Exception {
        // given
        Long projectId = 82425L;
        CreateMeetingPlaceRequest request = new CreateMeetingPlaceRequest();

        // when
        ResultActions perform = mockMvc.perform(post("/projects/" + projectId + "/meeting-places")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        // then
        perform
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message", containsString("must not be null")));
        verify(meetingPlaceService, never()).createMeetingPlace(anyLong(), any(CreateMeetingPlaceRequest.class));
    }

    @Test
    void 지도_바운더리에_해당하는_모임_장소들을_조회한다() throws Exception {
        // given
        Long projectId = 1L;
        Double minLng = 127.022, maxLng = 127.918, minLat = 36.383, maxLat = 37.489;
        Project project = ProjectProvider.createProject(5L);
        List<MeetingPlaceResponse> meetingPlaceList = Stream.of(MeetingPlaceProvider.createMeetingPlaceWithCoordinate(project, 127.523, 37.024))
                .map(MeetingPlaceResponse::new)
                .collect(Collectors.toList());

        given(meetingPlaceService.getSpecificAreaMeetingPlace(eq(projectId), any(SearchByMapBoundaryRequest.class)))
                .willReturn(meetingPlaceList);

        // when
        ResultActions perform = mockMvc.perform(get("/projects/" + projectId + "/meeting-places/boundary?minLng=" + minLng + "&maxLng=" + maxLng + "&minLat=" + minLat + "&maxLat=" + maxLat)
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(meetingPlaceList.size()))
                .andExpect(jsonPath("$[0].longitude").value(is(greaterThanOrEqualTo(minLng))))
                .andExpect(jsonPath("$[0].longitude").value(is(lessThanOrEqualTo(maxLng))))
                .andExpect(jsonPath("$[0].latitude").value(is(greaterThanOrEqualTo(minLat))))
                .andExpect(jsonPath("$[0].latitude").value(is(lessThanOrEqualTo(maxLat))));
        verify(meetingPlaceService, times(1)).getSpecificAreaMeetingPlace(anyLong(), any(SearchByMapBoundaryRequest.class));
    }

    @Test
    void 프로젝트의_모임_장소들을_조회한다() throws Exception {
        // given
        Long projectId = 82425L;
        List<String> placeNames = List.of("모임장소__", "테스트__", "짜는 중__");
        List<MeetingPlaceResponse> places = placeNames.stream()
                        .map(name-> new MeetingPlaceResponse(MeetingPlaceProvider.createMeetingPlaceWithName(name)))
                        .collect(Collectors.toList());

        given(meetingPlaceService.getMeetingPlaces(projectId))
                .willReturn(places);

        // when
        ResultActions perform = mockMvc.perform(get("/projects/" + projectId + "/meeting-places")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(placeNames.size()))
                .andExpect(jsonPath("$[*].name").value(containsInAnyOrder(placeNames.get(0), placeNames.get(1), placeNames.get(2))));
        verify(meetingPlaceService, times(1)).getMeetingPlaces(anyLong());;
    }

    @Test
    void 지도_바운더리의_정보가_모두_오지_않으면_모임_장소를_조회할_수_없다() throws Exception {
        // given
        Long projectId = 1L;

        // when
        ResultActions perform = mockMvc.perform(get("/projects/" + projectId + "/meeting-places/boundary?minLng=" + 127.052)
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        perform
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message", containsString("must not be null")));
    }

    @Test
    void 선택한_모임_장소를_삭제한다() throws Exception {
        // given
        Long meetingPlaceId = 3L;

        willDoNothing()
                .given(meetingPlaceService)
                .deleteMeetingPlace(eq(meetingPlaceId));

        // when
        ResultActions perform = mockMvc.perform(delete("/projects/meeting-places/" + meetingPlaceId)
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        perform
                .andExpect(status().isOk());
        verify(meetingPlaceService, times(1)).deleteMeetingPlace(anyLong());
    }

    @Test
    void 삭제하려는_모임_장소가_없을_경우_예외가_발생한다() throws Exception {
        // given
        Long meetingPlaceId = 2L;

        willThrow(new MeetingPlaceNotFoundException())
                .given(meetingPlaceService)
                .deleteMeetingPlace(eq(meetingPlaceId));

        // when
        ResultActions perform = mockMvc.perform(delete("/projects/meeting-places/" + meetingPlaceId)
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        perform
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("해당하는 모임 장소를 찾을 수 없습니다."));
    }

}
