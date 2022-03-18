package kr.kro.colla.meeting_place.meeting_place.presentation;

import kr.kro.colla.meeting_place.meeting_place.domain.MeetingPlace;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.CreateMeetingPlaceRequest;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.MeetingPlaceResponse;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.SearchByMapBoundaryRequest;
import kr.kro.colla.meeting_place.meeting_place.service.MeetingPlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class MeetingPlaceController {

    private final MeetingPlaceService meetingPlaceService;

    @PostMapping("/{projectId}/meeting-places")
    public ResponseEntity<MeetingPlaceResponse> createMeetingPlace(@PathVariable Long projectId,
                                                                    @Valid @RequestBody CreateMeetingPlaceRequest request) {
        MeetingPlace meetingPlace = meetingPlaceService.createMeetingPlace(projectId, request);

        return ResponseEntity.ok(new MeetingPlaceResponse(meetingPlace));
    }

    @GetMapping("/{projectId}/meeting-places")
    ResponseEntity<List<MeetingPlaceResponse>> getMeetingPlaces(@PathVariable Long projectId) {
        List<MeetingPlaceResponse> meetingPlaceList = meetingPlaceService.getMeetingPlaces(projectId);

        return ResponseEntity.ok(meetingPlaceList);
    }

    @GetMapping({"/{projectId}/meeting-places/boundary"})
    public ResponseEntity<List<MeetingPlaceResponse>> getSpecificAreaMeetingPlace(@PathVariable Long projectId,
                                                                                  @Valid SearchByMapBoundaryRequest request) {
        List<MeetingPlaceResponse> meetingPlaceList = meetingPlaceService.getSpecificAreaMeetingPlace(projectId, request);

        return ResponseEntity.ok(meetingPlaceList);
    }

    @DeleteMapping("/meeting-places/{meetingPlaceId}")
    public ResponseEntity<Void> deleteMeetingPlace(@PathVariable Long meetingPlaceId) {
        meetingPlaceService.deleteMeetingPlace(meetingPlaceId);

        return ResponseEntity.ok()
                .build();
    }
}
