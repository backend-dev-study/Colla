package kr.kro.colla.meeting_place.meeting_place.service;

import kr.kro.colla.exception.exception.meeting_place.MeetingPlaceNotFoundException;
import kr.kro.colla.meeting_place.meeting_place.domain.MeetingPlace;
import kr.kro.colla.meeting_place.meeting_place.domain.repository.MeetingPlaceRepository;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.CreateMeetingPlaceRequest;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.MeetingPlaceResponse;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.SearchByMapBoundaryRequest;
import kr.kro.colla.project.project.domain.Project;

import kr.kro.colla.project.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

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
                .category(request.getCategory())
                .project(project)
                .build();

        return meetingPlaceRepository.save(meetingPlace);
    }

    public List<MeetingPlaceResponse> getSpecificAreaMeetingPlace(Long projectId, SearchByMapBoundaryRequest request) {
        Project project = projectService.findProjectById(projectId);

        return meetingPlaceRepository.findMeetingPlacesByBoundary(project, request)
                .stream()
                .map(MeetingPlaceResponse::new)
                .collect(Collectors.toList());
    }

    public List<MeetingPlaceResponse> getMeetingPlaces(Long projectId) {
        Project project = projectService.findProjectById(projectId);

       return project.getMeetingPlaces().stream()
                .map(MeetingPlaceResponse::new)
                .collect(Collectors.toList());
    }

    public void deleteMeetingPlace(Long meetingPlaceId) {
        MeetingPlace meetingPlace = findMeetingPlaceById(meetingPlaceId);
        meetingPlaceRepository.delete(meetingPlace);
    }

    public MeetingPlace findMeetingPlaceById(Long id) {
        return meetingPlaceRepository.findById(id)
                .orElseThrow(MeetingPlaceNotFoundException::new);
    }

}
