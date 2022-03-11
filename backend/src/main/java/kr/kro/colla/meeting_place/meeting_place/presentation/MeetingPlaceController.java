package kr.kro.colla.meeting_place.meeting_place.presentation;

import kr.kro.colla.meeting_place.meeting_place.presentation.dto.CreateMeetingPlaceRequest;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.MeetingPlaceResponse;
import kr.kro.colla.meeting_place.meeting_place.service.MeetingPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class MeetingPlaceController {
    private final MeetingPlaceService meetingPlaceService;

    @PostMapping("/{projectId}/meeting-places")
    ResponseEntity<MeetingPlaceResponse> CreateMeetingPlace(@PathVariable Long projectId,
                                                            @Valid @RequestBody CreateMeetingPlaceRequest request) {
        MeetingPlaceResponse response = new MeetingPlaceResponse(meetingPlaceService.createMeetingPlace(request));

        return ResponseEntity.ok(response);
    }
}
