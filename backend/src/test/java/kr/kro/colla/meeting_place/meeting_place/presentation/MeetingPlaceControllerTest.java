package kr.kro.colla.meeting_place.meeting_place.presentation;

import kr.kro.colla.common.ControllerTest;
import kr.kro.colla.common.fixture.MeetingPlaceProvider;
import kr.kro.colla.meeting_place.meeting_place.domain.MeetingPlace;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.CreateMeetingPlaceRequest;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.MeetingPlaceResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.Cookie;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeetingPlaceController.class)
class MeetingPlaceControllerTest extends ControllerTest {

    @Test
    void 프로젝트의_모임_장소를_추가한다() throws Exception {
        // given
        Long projectId = 82425L, meetingPlaceId = 234241L;
        CreateMeetingPlaceRequest request = new CreateMeetingPlaceRequest("meeting place name", "preview image", 23.23, 3.5, "address to go!");
        MeetingPlace meetingPlace = MeetingPlaceProvider.createMeetingPlace();
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
        verify(meetingPlaceService, times(1)).getMeetingPlaces(anyLong());
    }
}