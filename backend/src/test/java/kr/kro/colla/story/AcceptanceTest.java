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

}
