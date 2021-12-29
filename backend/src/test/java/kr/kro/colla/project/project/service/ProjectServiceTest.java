package kr.kro.colla.project.project.service;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.repository.ProjectRepository;
import kr.kro.colla.project.project.service.dto.CreateRequest;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

class ProjectServiceTest {
    ProjectRepository projectRepository = mock(ProjectRepository.class);
    ProjectService projectService = new ProjectService(projectRepository);

    private Long managerId = 1L;
    private String name = "생성할 프로젝트 이름", desc = "생성할 프로젝트 설명";

    @Test
    void 프로젝트_생성_성공(){
        // given
        doAnswer(invocation -> {
            Project sample = Project.builder().managerId(managerId).name(name).description(desc).build();
            ReflectionTestUtils.setField(sample, "id", 1L);
            return sample;
        }).when(projectRepository).save(any(Project.class));

        // when
        Project project = projectService.createProject(managerId, new CreateRequest(name, desc));

        // then
        assertThat(project.getId()).isNotNull();
        assertThat(project.getManagerId()).isEqualTo(managerId);
        assertThat(project.getName()).isEqualTo(name);
        assertThat(project.getDescription()).isEqualTo(desc);
    }
    @Test
    void 프로젝트_생성_실패(){
    }
}