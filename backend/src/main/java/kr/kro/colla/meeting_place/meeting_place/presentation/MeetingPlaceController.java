package kr.kro.colla.meeting_place.meeting_place.presentation;

import kr.kro.colla.meeting_place.meeting_place.presentation.dto.CreateMeetingPlaceRequest;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.MeetingPlaceResponse;
import kr.kro.colla.meeting_place.meeting_place.service.MeetingPlaceService;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class MeetingPlaceController {
    private final MeetingPlaceService meetingPlaceService;
    private final ProjectService projectService;

    @PostMapping("/{projectId}/meeting-places")
    ResponseEntity<MeetingPlaceResponse> CreateMeetingPlace(@PathVariable Long projectId,
                                                            @Valid @RequestBody CreateMeetingPlaceRequest request) {
        MeetingPlaceResponse response = new MeetingPlaceResponse(meetingPlaceService.createMeetingPlace(projectId, request));

        return ResponseEntity.ok(response);
    }
}
