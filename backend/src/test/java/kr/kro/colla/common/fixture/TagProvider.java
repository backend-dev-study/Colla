package kr.kro.colla.common.fixture;

import io.restassured.http.ContentType;
import kr.kro.colla.project.project.presentation.dto.CreateTagRequest;
import kr.kro.colla.project.project.presentation.dto.ProjectTagResponse;
import kr.kro.colla.task.tag.domain.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;

@Component
public class TagProvider {

    public ProjectTagResponse 를_생성한다(String accessToken, Long projectId, String tagName) {
        CreateTagRequest createTagRequest = new CreateTagRequest(tagName);

        return given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(createTagRequest)
        .when()
                .post("/api/projects/" + projectId + "/tags")
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ProjectTagResponse.class);
    }

    public static Tag createTag(String tagName) {
        return new Tag(tagName);
    }

}
