package kr.kro.colla.story;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import kr.kro.colla.auth.service.JwtProvider;
import kr.kro.colla.common.database.DatabaseCleaner;
import kr.kro.colla.common.fixture.Auth;
import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.common.fixture.StoryProvider;
import kr.kro.colla.common.fixture.UserProvider;
import kr.kro.colla.project.project.presentation.dto.ProjectStorySimpleResponse;
import kr.kro.colla.story.presentation.dto.ProjectStoryResponse;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.presentation.dto.UserProjectResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserProvider user;

    @Autowired
    private ProjectProvider project;

    @Autowired
    private StoryProvider story;

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
    void 사용자가_프로젝트에_속한_모든_스토리를_조회한다() {
        // given
        User member = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(member.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        ProjectStorySimpleResponse story1 = story.를_생성한다(createdProject.getId(), accessToken, "first story");
        ProjectStorySimpleResponse story2 = story.를_생성한다(createdProject.getId(), accessToken, "second story");

        List<ProjectStoryResponse> response = given()
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + createdProject.getId() + "/all-stories")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<ProjectStoryResponse>>() {});

        assertThat(response).hasSize(2);
        assertThat(response.get(0).getTitle()).isEqualTo(story1.getTitle());
        assertThat(response.get(1).getTitle()).isEqualTo(story2.getTitle());
    }

    @Test
    void 사용자가_스토리의_진행_기간을_설정한다() {
        // given
        User member = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(member.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        story.를_생성한다(createdProject.getId(), accessToken, "first story");

        given()
                .contentType(ContentType.URLENC)
                .cookie("accessToken", accessToken)
                .formParam("startAt", "2022-03-09")
                .formParam("endAt", "2022-03-12")

        // when
        .when()
                .patch("/api/projects/stories/" + 1L + "/period")

        // then
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 사용자가_스토리의_진행_기간을_설정한_뒤_조회한다() {
        // given
        String startAt = "2022-03-09", endAt = "2022-03-12";
        User member = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(member.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        story.를_생성한다(createdProject.getId(), accessToken, "first story");
        story.의_진행_기간을_설정한다(1L, accessToken, startAt, endAt);

        List<ProjectStoryResponse> response = given()
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("api/projects/" + createdProject.getId() + "/all-stories")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<ProjectStoryResponse>>() {});

        assertThat(response.get(0).getStartAt()).isEqualTo(startAt);
        assertThat(response.get(0).getEndAt()).isEqualTo(endAt);
    }

    @Test
    void 사용자가_스토리_진행_기간_설정_시_아무것도_입력하지_않으면_설정에_실패한다() {
        // given
        User member = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(member.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        story.를_생성한다(createdProject.getId(), accessToken, "first story");

        given()
                .contentType(ContentType.URLENC)
                .header("Accept-Language", "ko")
                .cookie("accessToken", accessToken)

        // when
        .when()
                .patch("/api/projects/stories/" + 1L + "/period")

        // then
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", containsString("널이어서는 안됩니다"));
    }

}
