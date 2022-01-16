package kr.kro.colla.project.project.service;

import kr.kro.colla.common.fixture.FileProvider;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.profile.ProjectProfileStorage;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import kr.kro.colla.user.user.presentation.dto.CreateProjectRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectProfileStorage projectProfileStorage;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private Long id = 1L, managerId = 1L;
    private String name = "생성할 프로젝트 이름", desc = "생성할 프로젝트 설명";

    @Test
    void 프로젝트_생성을_성공한다() {
        // given
        String fileName = "thumbnail.png";
        MultipartFile thumbnail1 = FileProvider.getTestMultipartFile(fileName);
        CreateProjectRequest request = CreateProjectRequest.builder()
                .name(name)
                .description(desc)
                .thumbnail(thumbnail1)
                .build();
        Project project = Project.builder()
                .managerId(managerId)
                .name(name)
                .description(desc)
                .thumbnail(FileProvider.extractImageUrl(thumbnail1))
                .build();
        ReflectionTestUtils.setField(project, "id", id);
        ReflectionTestUtils.setField(project, "taskStatuses", List.of("To do", "In progress", "Done"));

        given(projectProfileStorage.upload(any(MultipartFile.class)))
                .willReturn(FileProvider.extractImageUrl(thumbnail1));
        given(projectRepository.save(any(Project.class)))
                .willReturn(project);

        // when
        Project result = projectService.createProject(managerId, request);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getManagerId()).isEqualTo(managerId);
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getDescription()).isEqualTo(desc);
        assertThat(result.getThumbnail()).isEqualTo(FileProvider.extractImageUrl(thumbnail1));
        assertThat(result.getTaskStatuses().size()).isEqualTo(3);

    }

}