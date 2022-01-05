package kr.kro.colla.project.project.service;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.user.user.presentation.dto.CreateProjectRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    @Mock
    ProjectRepository projectRepository;

    @InjectMocks
    ProjectService projectService;

    private Long id = 1L, managerId = 1L;
    private String name = "생성할 프로젝트 이름", desc = "생성할 프로젝트 설명";

    @Test
    void 프로젝트_생성을_성공한다() {
        // given
        CreateProjectRequest request = CreateProjectRequest.builder()
                .name(name)
                .description(desc)
                .build();
        Project sample = Project.builder()
                .managerId(managerId)
                .name(name)
                .description(desc)
                .build();
        ReflectionTestUtils.setField(sample, "id", id);
        ReflectionTestUtils.setField(sample, "taskStatuses", new ArrayList<>(Arrays.asList("To do", "In progress", "Done")));

        given(projectRepository.save(any(Project.class))).willReturn(sample);

        // when
        Project project = projectService.createProject(managerId, request);

        // then
        assertThat(project.getId()).isNotNull();
        assertThat(project.getManagerId()).isEqualTo(managerId);
        assertThat(project.getName()).isEqualTo(name);
        assertThat(project.getDescription()).isEqualTo(desc);
        assertThat(project.getTaskStatuses().size()).isEqualTo(3);

    }

}