package kr.kro.colla.common.fixture;

import io.restassured.http.ContentType;
import kr.kro.colla.comment.domain.Comment;
import kr.kro.colla.comment.presentation.dto.CreateCommentRequest;
import kr.kro.colla.comment.presentation.dto.CreateCommentResponse;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.user.user.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;

@Component
public class CommentProvider {

    public CreateCommentResponse 를_등록한다(String accessToken) {
        CreateCommentRequest createCommentRequest = new CreateCommentRequest(1L, null, "comment contents");

        return given()
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)
                .body(createCommentRequest)
        .when()
                .post("/api/task/comments")
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(CreateCommentResponse.class);
    }

    public static Comment createComment1(User user, Task task, Comment superComment) {
        return Comment.builder()
                .user(user)
                .task(task)
                .superComment(superComment)
                .contents("first comment contents")
                .build();
    }

    public static Comment createComment2(User user, Task task, Comment superComment) {
        return Comment.builder()
                .user(user)
                .task(task)
                .superComment(superComment)
                .contents("second comment contents")
                .build();
    }

}
