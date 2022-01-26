package kr.kro.colla.user.user;

import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import kr.kro.colla.auth.service.JwtProvider;
import kr.kro.colla.common.fixture.Auth;
import kr.kro.colla.common.fixture.FileProvider;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import kr.kro.colla.user.notice.domain.Notice;
import kr.kro.colla.user.notice.domain.NoticeType;
import kr.kro.colla.user.notice.domain.repository.NoticeRepository;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.domain.repository.UserRepository;

import kr.kro.colla.user.user.presentation.dto.UpdateUserNameRequest;
import kr.kro.colla.user.user.presentation.dto.UserProjectResponse;
import kr.kro.colla.user_project.domain.UserProject;
import kr.kro.colla.user_project.domain.repository.UserProjectRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserProjectRepository userProjectRepository;
    @Autowired
    private NoticeRepository noticeRepository;

    private Auth auth;
    private User user;
    private String accessToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        auth = new Auth(jwtProvider);

        user = User.builder()
                .githubId("binimini")
                .name("subin")
                .avatar("github_content")
                .build();
        userRepository.save(user);

        accessToken = auth.로그인(user.getId());
    }

    @AfterEach
    void rollback() {
        userProjectRepository.deleteAll();
        projectRepository.deleteAll();
        noticeRepository.deleteAll();
        userRepository.deleteAll();
    }
    @Test
    void 로그인한_사용자의_프로필을_조회한다() {
        // given
        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
        // when
        .when()
                .get("/api/users/profile")
        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("displayName", equalTo(user.getName()))
                .body("githubId", equalTo(user.getGithubId()))
                .body("avatar", equalTo(user.getAvatar()));
    }

    @Test
    void 사용자_프로젝트_생성_성공_후_반환한다() throws IOException {
        // given
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
                .body("managerId", equalTo(user.getId().intValue()))
                .body("name", equalTo(name))
                .body("description", equalTo(desc));
    }

    @Test
    void 사용자_프로젝트_생성_시_요청이_잘못돼_에러를_반환한다() throws Exception {
        // given
        String name = "프로젝트 이름", desc = "프로젝트 설명";
        MockMultipartFile thumbnail = FileProvider.getTestMultipartFile("thumbnail.png");
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("description", desc);

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
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("name : 공백일 수 없습니다"));
    }

    @Test
    void 사용자는_이름을_변경할_수_있다() {
        // given
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
    void 사용자는_자신이_참여중인_프로젝트_목록을_조회할_수_있다() {
        // given
        Project project1 = Project.builder()
                .managerId(user.getId())
                .name("project1")
                .description("project1 description")
                .build();
        Project project2 = Project.builder()
                .managerId(user.getId())
                .name("project2")
                .description("project2 description")
                .build();
        UserProject userProject1 = UserProject.builder()
                .user(user)
                .project(project1)
                .build();
        UserProject userProject2 = UserProject.builder()
                .user(user)
                .project(project2)
                .build();
        projectRepository.save(project1);
        projectRepository.save(project2);
        userProjectRepository.save(userProject1);
        userProjectRepository.save(userProject2);

        List<UserProjectResponse> response = given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", this.accessToken)

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
        assertThat(response.get(0).getName()).isEqualTo(project1.getName());
        assertThat(response.get(1).getDescription()).isEqualTo(project2.getDescription());
        assertThat(response.get(0).getManagerId()).isEqualTo(user.getId());
        assertThat(response.get(1).getManagerId()).isEqualTo(user.getId());
    }

    @Transactional
    @Test
    void 사용자는_사용자의_알림들을_조회할_수_있다(){
        // given
        List<Notice> data = new ArrayList<>();
        Notice notice1 = Notice.builder()
                .noticeType(NoticeType.INVITE_USER)
                .build();
        Notice notice2 = Notice.builder()
                .noticeType(NoticeType.MENTION_USER)
                .mentionedURL("test/url/for/mention")
                .build();
        data.add(notice1);
        data.add(notice2);

        noticeRepository.save(notice1);
        noticeRepository.save(notice2);

        User receiver = userRepository.findById(user.getId()).get();
        receiver.addNotice(notice1);
        receiver.addNotice(notice2);

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

        IntStream
                .range(0, response.size())
                .forEach(i->{
                            assertThat(response.get(i).getId()).isEqualTo(data.get(i).getId());
                            assertThat(response.get(i).getNoticeType()).isEqualTo(data.get(i).getNoticeType());
                            assertThat(response.get(i).getIsChecked()).isEqualTo(data.get(i).getIsChecked());
                            assertThat(response.get(i).getMentionedURL()).isEqualTo(data.get(i).getMentionedURL());
                    });

    }
}
