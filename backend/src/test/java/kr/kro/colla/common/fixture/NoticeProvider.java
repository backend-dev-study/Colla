package kr.kro.colla.common.fixture;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import kr.kro.colla.project.project.presentation.dto.ProjectMemberRequest;
import kr.kro.colla.user.notice.domain.Notice;
import kr.kro.colla.user.notice.domain.NoticeType;
import kr.kro.colla.user.user.presentation.dto.UserNoticeResponse;
import org.aspectj.weaver.ast.Not;
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

    public static Notice createInviteNotice() {
        return Notice.builder()
                .noticeType(NoticeType.INVITE_USER)
                .projectName("project to invite")
                .projectId(234243L)
                .build();
    }

    public static Notice createMentionNotice() {
        return Notice.builder()
                .noticeType(NoticeType.MENTION_USER)
                .mentionedURL("url to mention")
                .build();
    }

}
