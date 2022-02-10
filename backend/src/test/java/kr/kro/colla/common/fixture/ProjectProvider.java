package kr.kro.colla.common.fixture;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.http.ContentType;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.user.user.presentation.dto.UserProjectResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@Component
public class ProjectProvider {

    public UserProjectResponse 를_생성한다(String accessToken) {
        try {
            MockMultipartFile thumbnail = FileProvider.getTestMultipartFile("thumbnail.png");
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("name", "collaboration");
            requestBody.put("description", "collaboration tool");

            return given()
                    .contentType(ContentType.MULTIPART)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .cookie("accessToken", accessToken)
                    .multiPart(new MultiPartSpecBuilder(thumbnail.getBytes())
                            .controlName("thumbnail")
                            .mimeType("image/png")
                            .build())
                    .formParams(requestBody)
            .when()
                    .post("/api/users/projects")
            .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(UserProjectResponse.class);
        } catch (IOException e) {
            return null;
        }
    }

    public static Project createProject(Long managerId) {
        return Project.builder()
                .managerId(managerId)
                .name("collaboration")
                .description("collaboration tool")
                .thumbnail("s3_content")
                .build();
    }

}
