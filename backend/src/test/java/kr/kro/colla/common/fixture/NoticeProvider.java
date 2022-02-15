package kr.kro.colla.common.fixture;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import kr.kro.colla.project.project.presentation.dto.ProjectMemberRequest;
import kr.kro.colla.user.user.presentation.dto.UserNoticeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.restassured.RestAssured.given;

@Component
public class NoticeProvider {

    public void 를_생성한다(String accessToken, Long projectId, String memberGithubId) {
        ProjectMemberRequest projectMemberRequest = new ProjectMemberRequest(memberGithubId);

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(projectMemberRequest)
        .when()
                .post("/api/projects/" + projectId + "/members")
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    public List<UserNoticeResponse> 를_조회한다(String accessToken) {
        return given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
        .when()
                .get("/api/users/notices")
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(new TypeRef<List<UserNoticeResponse>>() {});
    }

}
