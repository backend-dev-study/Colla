package kr.kro.colla.common.fixture;

import io.restassured.http.ContentType;
import kr.kro.colla.project.project.presentation.dto.CreateTaskStatusRequest;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;

@Component
public class TaskStatusProvider {

    public void 를_생성한다(String accessToken, Long projectId, String statusName) {
        CreateTaskStatusRequest request = new CreateTaskStatusRequest(statusName);

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(request)
        .when()
                .post("/api/projects/" + projectId + "/statuses")
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    public static TaskStatus createTaskStatus(String name) {
        return new TaskStatus(name);
    }
}
