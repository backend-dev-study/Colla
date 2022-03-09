package kr.kro.colla.common.fixture;

import io.restassured.http.ContentType;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.presentation.dto.CreateStoryRequest;
import kr.kro.colla.project.project.presentation.dto.ProjectStorySimpleResponse;
import kr.kro.colla.story.domain.Story;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;

@Component
public class StoryProvider {

    public ProjectStorySimpleResponse 를_생성한다(Long projectId, String accessToken, String title) {
        CreateStoryRequest createStoryRequest = new CreateStoryRequest(title);

        return given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(createStoryRequest)
        .when()
                .post("/api/projects/" + projectId + "/stories")
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ProjectStorySimpleResponse.class);
    }

    public static Story createStory(Project project, String title) {
        return Story.builder()
                .title(title)
                .preStories("[]")
                .project(project)
                .build();
    }

}
