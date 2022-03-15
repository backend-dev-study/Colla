package kr.kro.colla.common.fixture;

import kr.kro.colla.meeting_place.meeting_place.domain.MeetingPlace;
import kr.kro.colla.project.project.domain.Project;

public class MeetingPlaceProvider {

    public static MeetingPlace createMeetingPlace(Project project) {
        MeetingPlace meetingPlace = MeetingPlace.builder()
                .name("이번 회의 장소")
                .longitude(134.234)
                .latitude(32.234)
                .address("서울특별시 강남구 ~~로 ~~번길")
                .project(project)
                .build();

        return meetingPlace;
    }

    public static MeetingPlace createMeetingPlaceWithCoordinate(Project project, Double longitude, Double latitude) {
        MeetingPlace meetingPlace = MeetingPlace.builder()
                .name("이번 회의 장소")
                .longitude(longitude)
                .latitude(latitude)
                .address("서울특별시 강남구 ~~로 ~~번길")
                .project(project)
                .build();

        return meetingPlace;
    }
}
