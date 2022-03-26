package kr.kro.colla.meeting_place.meeting_place;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import kr.kro.colla.auth.service.JwtProvider;
import kr.kro.colla.common.database.DatabaseCleaner;
import kr.kro.colla.common.fixture.*;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.CreateMeetingPlaceRequest;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.MeetingPlaceResponse;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.presentation.dto.UserProjectResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static kr.kro.colla.common.fixture.MeetingPlaceProvider.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserProvider user;

    @Autowired
    private ProjectProvider project;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    private Auth auth;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        auth = new Auth(jwtProvider);
        databaseCleaner.execute();
    }

    @Test
    void 사용자는_프로젝트의_모임_장소를_추가할_수_있다() {
        // given
        String placeName = "새로운 만날 장소 이름", placeImage = "https://image/url/of/place", placeAddress = "SeongNam";
        Double placeLng = 23.4141, placeLat = 194.145;
        User loginUser = user.가_로그인을_한다2();
        String accessToken = auth.토큰을_발급한다(loginUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        CreateMeetingPlaceRequest request = new CreateMeetingPlaceRequest(placeName, placeImage, placeLng, placeLat, placeAddress, "category");

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(request)

        // when
        .when()
                .post("/api/projects/" + createdProject.getId() + "/meeting-places")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo(placeName))
                .body("image", equalTo(placeImage))
                .body("address", equalTo(placeAddress))
                .body("longitude", equalTo(placeLng.floatValue()))
                .body("latitude", equalTo(placeLat.floatValue()));
    }

    @Test
    void 사용자의_요청_정보가_부족하면_모임_장소를_추가할_수_없다() {
        //given
        User loginUser = user.가_로그인을_한다2();
        String accessToken = auth.토큰을_발급한다(loginUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        CreateMeetingPlaceRequest request = new CreateMeetingPlaceRequest();

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(request)

        // when
        .when()
                .post("/api/projects/" + createdProject.getId() + "/meeting-places")

        // then
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", containsString("널이어서는 안됩니다"));
    }

    @Test
    void 사용자가_드래그한_바운더리에_속하는_모임_장소들을_조회한다() {
        // given
        Double minLng = 127.022, maxLng = 127.918, minLat = 36.383, maxLat = 37.489;
        User member = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(member.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        모임_장소를_생성한다(accessToken, createdProject.getId(), 127.992, 36.813);
        모임_장소를_생성한다(accessToken, createdProject.getId(),127.52, 37.174);
        모임_장소를_생성한다(accessToken, createdProject.getId(), 127.318, 37.769);

        List<MeetingPlaceResponse> response = given()
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + createdProject.getId() + "/meeting-places/boundary?minLng=" + minLng + "&maxLng=" + maxLng + "&minLat=" + minLat + "&maxLat=" + maxLat)

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<MeetingPlaceResponse>>() {});

        assertThat(response).hasSize(1);
        assertThat(response.get(0).getLongitude()).isGreaterThanOrEqualTo(minLng).isLessThanOrEqualTo(maxLng);
        assertThat(response.get(0).getLatitude()).isGreaterThanOrEqualTo(minLat).isLessThanOrEqualTo(maxLat);
    }

    @Test
    void 사용자는_프로젝트의_모임_장소들을_조회한다() {
        // given
        User loginUser = user.가_로그인을_한다2();
        String accessToken = auth.토큰을_발급한다(loginUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        List<MeetingPlaceResponse> placeList = List.of(
                모임_장소를_생성한다(accessToken, createdProject.getId(), 123.523, 156.1),
                모임_장소를_생성한다(accessToken, createdProject.getId(), 34.53, 1.45),
                모임_장소를_생성한다(accessToken, createdProject.getId(), 23.51, 5.51)
        );

        List<MeetingPlaceResponse> responses = given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + createdProject.getId() + "/meeting-places")

        // then
        .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<MeetingPlaceResponse>>() {});

        IntStream.range(0, responses.size())
                .forEach(i -> {
                    assertThat(responses.get(i).getId()).isEqualTo(placeList.get(i).getId());
                    assertThat(responses.get(i).getName()).isEqualTo(placeList.get(i).getName());
                    assertThat(responses.get(i).getLongitude()).isEqualTo(placeList.get(i).getLongitude());
                    assertThat(responses.get(i).getLatitude()).isEqualTo(placeList.get(i).getLatitude());
                });
    }

    @Test
    void 사용자가_특정_모임_장소를_목록에서_삭제한다() {
        // given
        User member = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(member.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        모임_장소를_생성한다(accessToken, createdProject.getId(), 127.318, 37.769);

        given()
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .delete("api/projects/meeting-places/" + 1L)

        // then
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 사용자는_존재하지_않은_모임_장소를_삭제할_수_없다() {
        // given
        User member = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(member.getId());

        given()
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .delete("/api/projects/meeting-places/" + 1L)

        // then
        .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("status", equalTo(HttpStatus.NOT_FOUND.value()))
                .body("message", equalTo("해당하는 모임 장소를 찾을 수 없습니다."));
    }

}
