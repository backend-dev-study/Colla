package kr.kro.colla.common.fixture;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import kr.kro.colla.meeting_place.meeting_place.domain.MeetingPlace;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.CreateMeetingPlaceRequest;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.MeetingPlaceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;

@Component
public class MeetingPlaceProvider {

    public MeetingPlaceResponse 를_생성한다(String accessToken, Long projectId, Double longitude, Double latitude) {
        CreateMeetingPlaceRequest request = new CreateMeetingPlaceRequest("name", "image", longitude, latitude, "address");

        return given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(request)
        .when()
                .post("/api/projects/" + projectId + "/meeting-places")
        .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<MeetingPlaceResponse>() {});
    }

    public static MeetingPlace createMeetingPlace() {
        MeetingPlace meetingPlace = MeetingPlace.builder()
                .name("이번 회의 장소")
                .longitude(134.234)
                .latitude(32.234)
                .address("서울특별시 강남구 ~~로 ~~번길")
                .build();

        return meetingPlace;
    }

    public static MeetingPlace createMeetingPlaceWithName(String name) {
        MeetingPlace meetingPlace = MeetingPlace.builder()
                .name(name)
                .longitude(134.234)
                .latitude(32.234)
                .address("서울특별시 강남구 ~~로 ~~번길")
                .build();

        return meetingPlace;
    }
}
