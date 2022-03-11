package kr.kro.colla.meeting_place.meeting_place.service;

import kr.kro.colla.meeting_place.meeting_place.domain.MeetingPlace;
import kr.kro.colla.meeting_place.meeting_place.domain.repository.MeetingPlaceRepository;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.CreateMeetingPlaceRequest;
import kr.kro.colla.project.project.domain.Project;

import kr.kro.colla.project.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MeetingPlaceService {
    private final MeetingPlaceRepository meetingPlaceRepository;

    private final ProjectService projectService;

    public MeetingPlace createMeetingPlace(Long projectId, CreateMeetingPlaceRequest request) {
        Project project = projectService.findProjectById(projectId);

        MeetingPlace meetingPlace = MeetingPlace.builder()
                .name(request.getName())
                .image(request.getImage())
                .longitude(request.getLongitude())
                .latitude(request.getLatitude())
                .address(request.getAddress())
                .build();

        MeetingPlace result = meetingPlaceRepository.save(meetingPlace);
        project.addMeetingPlace(result);

        return result;
    }

}
