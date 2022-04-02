package kr.kro.colla.task.task_status_log;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import kr.kro.colla.auth.service.JwtProvider;
import kr.kro.colla.common.database.DatabaseCleaner;
import kr.kro.colla.common.fixture.Auth;
import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.common.fixture.TaskProvider;
import kr.kro.colla.common.fixture.UserProvider;
import kr.kro.colla.task.task_status_log.presentation.dto.TaskStatusLogResponse;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.presentation.dto.UserProjectResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.*;

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
    private TaskProvider task;

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
    void 사용자가_대시_보드_페이지에서_상태_로그_그래프를_확인한다() {
        // given
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(6);
        User member = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(member.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        task.를_특정_상태로_생성한다(accessToken, member.getId(), createdProject.getId(), null, "To Do");
        task.를_특정_상태로_생성한다(accessToken, member.getId(), createdProject.getId(), null, "In Progress");

        Map<String, List<TaskStatusLogResponse>> response = given()
                .cookie("accessToken", accessToken)
                .contentType(ContentType.JSON)

        // when
        .when()
                .get("/api/projects/" + createdProject.getId() + "/task-status-log")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<Map<String, List<TaskStatusLogResponse>>>() {});

        assertThat(response).hasSize(3);
        assertThat(response.get("To Do").get(0).getCreatedAt()).isAfterOrEqualTo(start).isBeforeOrEqualTo(end);
        assertThat(response.get("In Progress").get(0).getCreatedAt()).isAfterOrEqualTo(start).isBeforeOrEqualTo(end);
    }

}
