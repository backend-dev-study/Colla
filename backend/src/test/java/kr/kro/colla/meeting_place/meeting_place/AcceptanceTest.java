package kr.kro.colla.meeting_place.meeting_place;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import kr.kro.colla.auth.service.JwtProvider;
import kr.kro.colla.common.database.DatabaseCleaner;
import kr.kro.colla.common.fixture.*;
import kr.kro.colla.meeting_place.meeting_place.presentation.dto.CreateMeetingPlaceRequest;
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

import static io.restassured.RestAssured.given;
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
        Double placeLong = 23.4141, placeLa = 194.145;
        User loginUser = user.가_로그인을_한다2();
        String accessToken = auth.토큰을_발급한다(loginUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        CreateMeetingPlaceRequest request = new CreateMeetingPlaceRequest(placeName, placeImage, placeLong, placeLa, placeAddress);

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
                .body("longitude", equalTo(placeLong.floatValue()))
                .body("latitude", equalTo(placeLa.floatValue()));
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

}
