package kr.kro.colla.user.user;

import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import kr.kro.colla.auth.service.JwtProvider;
import kr.kro.colla.common.database.DatabaseCleaner;
import kr.kro.colla.common.fixture.*;
import kr.kro.colla.user.notice.domain.Notice;
import kr.kro.colla.user.notice.domain.NoticeType;
import kr.kro.colla.user.user.domain.User;

import kr.kro.colla.user.user.presentation.dto.UpdateUserNameRequest;
import kr.kro.colla.user.user.presentation.dto.UserProjectResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

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
    private NoticeProvider notice;

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
    void ????????????_?????????_????????????_????????????() {
        // given
        User registeredUser = user.???_????????????_??????2();
        String accessToken = auth.?????????_????????????(registeredUser.getId());

        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
        // when
        .when()
                .get("/api/users/profile")
        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("displayName", equalTo(registeredUser.getName()))
                .body("githubId", equalTo(registeredUser.getGithubId()))
                .body("avatar", equalTo(registeredUser.getAvatar()));
    }

    @Test
    void ????????????_???????????????_????????????() throws IOException {
        // given
        User registeredUser = user.???_????????????_??????2();
        String accessToken = auth.?????????_????????????(registeredUser.getId());

        String name = "project name", desc = "project description";
        MockMultipartFile thumbnail = FileProvider.getTestMultipartFile("thumbnail.png");
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", name);
        requestBody.put("description",desc);

        given()
                .contentType(ContentType.MULTIPART)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .multiPart(new MultiPartSpecBuilder(thumbnail.getBytes())
                        .controlName("thumbnail")
                        .mimeType("image/png")
                        .build())
                .formParams(requestBody)
        // when
        .when()
                .post("/api/users/projects")
        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("managerId", equalTo(registeredUser.getId().intValue()))
                .body("name", equalTo(name))
                .body("description", equalTo(desc));
    }

    @Test
    void ????????????_????????????_??????_?????????_?????????_???_????????????_?????????_????????????() throws Exception {
        // given
        User registeredUser = user.???_????????????_??????2();
        String accessToken = auth.?????????_????????????(registeredUser.getId());

        String name = "???????????? ??????", desc = "???????????? ??????";
        MockMultipartFile thumbnail = FileProvider.getTestMultipartFile("thumbnail.png");
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("description", desc);

        given()
                .contentType(ContentType.MULTIPART)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Accept-Language", "ko")
                .cookie("accessToken", accessToken)
                .multiPart(new MultiPartSpecBuilder(thumbnail.getBytes())
                        .controlName("thumbnail")
                        .mimeType("image/png")
                        .build())
                .formParams(requestBody)
        // when
        .when()
                .post("/api/users/projects")

        // then
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("name : ????????? ??? ????????????"));
    }

    @Test
    void ????????????_?????????_?????????_???_??????() {
        // given
        User registeredUser = user.???_????????????_??????2();
        String accessToken = auth.?????????_????????????(registeredUser.getId());

        String newDisplayName = "new-name";
        UpdateUserNameRequest updateUserNameRequest = new UpdateUserNameRequest(newDisplayName);

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(updateUserNameRequest)
        // when
        .when()
                .patch("/api/users/name")
        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo(newDisplayName));
    }

    @Test
    void ????????????_?????????_????????????_????????????_?????????_?????????_???_??????() {
        // given
        User registeredUser = user.???_????????????_??????2();
        String accessToken = auth.?????????_????????????(registeredUser.getId());
        UserProjectResponse createdProject1 = project.???_????????????(accessToken);
        UserProjectResponse createdProject2 = project.???_????????????(accessToken);

        List<UserProjectResponse> response = given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/users/projects")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<UserProjectResponse>>() {});

        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).getName()).isEqualTo(createdProject1.getName());
        assertThat(response.get(1).getDescription()).isEqualTo(createdProject2.getDescription());
        assertThat(response.get(0).getManagerId()).isEqualTo(registeredUser.getId());
        assertThat(response.get(1).getManagerId()).isEqualTo(registeredUser.getId());
    }

    @Test
    void ????????????_?????????_????????????_?????????_???_??????(){
        // given
        User registeredUser = user.???_????????????_??????1();
        String accessToken = auth.?????????_????????????(registeredUser.getId());
        UserProjectResponse createdProject = project.???_????????????(accessToken);
        notice.???_????????????(accessToken, createdProject.getId(), registeredUser.getGithubId());
        notice.???_????????????(accessToken, createdProject.getId(), registeredUser.getGithubId());

        List<Notice> response = given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/users/notices")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<Notice>>() {});

        assertThat(response.size()).isEqualTo(2);
        response.forEach(notice -> {
            assertThat(notice.getId()).isNotNull();
            assertThat(notice.getNoticeType()).isEqualTo(NoticeType.INVITE_USER);
            assertThat(notice.getProjectId()).isEqualTo(createdProject.getId());
            assertThat(notice.getProjectName()).isEqualTo(createdProject.getName());
        });
    }
}
