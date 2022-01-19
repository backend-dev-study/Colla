package kr.kro.colla.project.project;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import kr.kro.colla.auth.service.JwtProvider;
import kr.kro.colla.common.fixture.Auth;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import kr.kro.colla.project.project.presentation.dto.ProjectResponse;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.domain.repository.UserRepository;
import kr.kro.colla.user_project.service.UserProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProjectService userProjectService;

    private Auth auth;
    private User user;
    private Project project;
    private String accessToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        user = User.builder()
                .githubId("kykapple")
                .name("kyk")
                .avatar("github_content")
                .build();
        userRepository.save(user);
        project = Project.builder()
                .managerId(user.getId())
                .name("project name")
                .description("project description")
                .thumbnail("s3_content")
                .build();
        projectRepository.save(project);
        userProjectService.joinProject(user, project);

        auth = new Auth(jwtProvider);
        accessToken = auth.로그인(1L);
    }

    @Test
    void 프로젝트_목록에서_클릭한_프로젝트를_조회한다() {
        // given
        Long projectId = 1L;

        ProjectResponse response = given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + projectId)

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("name", equalTo(project.getName()))
                .body("description", equalTo(project.getDescription()))
                .body("members.size()", is(1))
                .extract()
                .body()
                .as(ProjectResponse.class);
        assertThat(response.getMembers().get(0).getGithubId()).isEqualTo(user.getGithubId());
        assertThat(response.getTasks().size()).isEqualTo(3);
        assertThat(response.getTasks().containsKey("To Do")).isTrue();
        assertThat(response.getTasks().containsKey("In Progress")).isTrue();
        assertThat(response.getTasks().containsKey("Done")).isTrue();
    }

}
