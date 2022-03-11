package kr.kro.colla.meeting_place.meeting_place.service;

import kr.kro.colla.meeting_place.meeting_place.domain.MeetingPlace;
import kr.kro.colla.meeting_place.meeting_place.domain.repository.MeetingPlaceRepository;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.CreateMeetingPlaceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MeetingPlaceService {
    private final MeetingPlaceRepository meetingPlaceRepository;

    public MeetingPlace createMeetingPlace(CreateMeetingPlaceRequest request) {
        MeetingPlace meetingPlace = MeetingPlace.builder()
                .name(request.getName())
                .image(request.getImage())
                .longitude(request.getLongitude())
                .latitude(request.getLatitude())
                .address(request.getAddress())
                .build();

        return meetingPlaceRepository.save(meetingPlace);
    }

}
