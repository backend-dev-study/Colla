package kr.kro.colla.common.fixture;

import io.restassured.http.ContentType;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.story.domain.Story;
import kr.kro.colla.task.task.domain.Task;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@Component
public class TaskProvider {

    public Map<String, String> 를_생성한다(String accessToken, Long managerId, Long projectId, String story) {
        Map<String, String> formData = new HashMap<>();
        formData.put("title", "task title");
        formData.put("description", "task description");
        formData.put("managerId", managerId != null ? managerId.toString() : null);
        formData.put("priority", "3");
        formData.put("status", "To Do");
        formData.put("tags", "[\"backend\"]");
        formData.put("projectId", projectId.toString());
        formData.put("story", story);
        formData.put("preTasks", "[]");

        given()
                .contentType(ContentType.URLENC)
                .cookie("accessToken", accessToken)
                .formParams(formData)
        .when()
                .post("/api/projects/tasks")
        .then()
                .statusCode(HttpStatus.CREATED.value());

        return formData;
    }

    public Map<String, String> 를_특정_제목과_함께_생성한다(String accessToken, Long managerId, Long projectId, String story, String title) {
        Map<String, String> formData = new HashMap<>();
        formData.put("title", title);
        formData.put("description", "task description");
        formData.put("managerId", managerId != null ? managerId.toString() : null);
        formData.put("priority", "3");
        formData.put("status", "To Do");
        formData.put("tags", "[\"backend\"]");
        formData.put("projectId", projectId.toString());
        formData.put("story", story);
        formData.put("preTasks", "[]");

        given()
                .contentType(ContentType.URLENC)
                .cookie("accessToken", accessToken)
                .formParams(formData)
        .when()
                .post("/api/projects/tasks")
        .then()
                .statusCode(HttpStatus.CREATED.value());

        return formData;
    }

    public Map<String, String> 를_특정_우선순위로_생성한다(String accessToken, Long managerId, Long projectId, String story, int priority) {
        Map<String, String> formData = new HashMap<>();
        formData.put("title", "task title");
        formData.put("description", "task description");
        formData.put("managerId", managerId != null ? managerId.toString() : null);
        formData.put("priority", String.valueOf(priority));
        formData.put("status", "To Do");
        formData.put("tags", "[\"backend\"]");
        formData.put("projectId", projectId.toString());
        formData.put("story", story);
        formData.put("preTasks", "[]");

        given()
                .contentType(ContentType.URLENC)
                .cookie("accessToken", accessToken)
                .formParams(formData)
        .when()
                .post("/api/projects/tasks")
        .then()
                .statusCode(HttpStatus.CREATED.value());

        return formData;
    }

    public Map<String, String> 를_특정_상태로_생성한다(String accessToken, Long managerId, Long projectId, String story, String statusName) {
        Map<String, String> formData = new HashMap<>();
        formData.put("title", "task title");
        formData.put("description", "task description");
        formData.put("managerId", managerId != null ? managerId.toString() : null);
        formData.put("priority", "3");
        formData.put("status", statusName);
        formData.put("tags", "[\"backend\"]");
        formData.put("projectId", projectId.toString());
        formData.put("story", story);
        formData.put("preTasks", "[]");

        given()
                .contentType(ContentType.URLENC)
                .cookie("accessToken", accessToken)
                .formParams(formData)
        .when()
                .post("/api/projects/tasks")
        .then()
                .statusCode(HttpStatus.CREATED.value());

        return formData;
    }

    public Map<String, String> 를_특정_태그와_함께_생성한다(String accessToken, Long managerId, Long projectId, String story, String tags) {
        Map<String, String> formData = new HashMap<>();
        formData.put("title", "task title");
        formData.put("description", "task description");
        formData.put("managerId", managerId != null ? managerId.toString() : null);
        formData.put("priority", "2");
        formData.put("status", "To Do");
        formData.put("tags", tags);
        formData.put("projectId", projectId.toString());
        formData.put("story", story);
        formData.put("preTasks", "[]");

        given()
                .contentType(ContentType.URLENC)
                .cookie("accessToken", accessToken)
                .formParams(formData)
        .when()
                .post("/api/projects/tasks")
        .then()
                .statusCode(HttpStatus.CREATED.value());

        return formData;
    }

    public static Task createTask(Long managerId, Project project, Story story) {
        return Task.builder()
                .title("task title")
                .managerId(managerId)
                .description("task description")
                .priority(4)
                .project(project)
                .taskStatus(new TaskStatus("To Do"))
                .story(story)
                .preTasks("[]")
                .build();
    }

    public static Task createTaskWithTitle(Long managerId, Project project, Story story, String title) {
        return Task.builder()
                .title(title)
                .managerId(managerId)
                .description("task description")
                .priority(4)
                .project(project)
                .taskStatus(new TaskStatus("To Do"))
                .story(story)
                .preTasks("[]")
                .build();
    }

    public static Task createTaskWithPriority(Long managerId, Project project, Story story, int priority) {
        return Task.builder()
                .title("task title")
                .managerId(managerId)
                .description("task description")
                .priority(priority)
                .project(project)
                .taskStatus(new TaskStatus("To Do"))
                .story(story)
                .preTasks("[]")
                .build();
    }

    public static Task createTaskForRepository(Long managerId, Project project, Story story, TaskStatus taskStatus) {
        return Task.builder()
                .title("task title")
                .managerId(managerId)
                .description("task description")
                .priority(4)
                .project(project)
                .taskStatus(taskStatus)
                .story(story)
                .preTasks("[]")
                .build();
    }

    public static Task createTaskWithTitleForRepository(Long managerId, Project project, Story story, TaskStatus taskStatus, String title) {
        return Task.builder()
                .title(title)
                .managerId(managerId)
                .description("task description")
                .priority(4)
                .project(project)
                .taskStatus(taskStatus)
                .story(story)
                .preTasks("[]")
                .build();
    }

    public static Task createTaskWithPriorityForRepository(Long managerId, Project project, Story story, TaskStatus taskStatus, int priority) {
        return Task.builder()
                .title("task title")
                .managerId(managerId)
                .description("task description")
                .priority(priority)
                .project(project)
                .taskStatus(taskStatus)
                .story(story)
                .preTasks("[]")
                .build();
    }

}
