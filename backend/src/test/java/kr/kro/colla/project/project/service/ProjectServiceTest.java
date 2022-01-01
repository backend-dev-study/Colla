package kr.kro.colla.project.project.service;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import kr.kro.colla.project.project.service.dto.CreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

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
    void 프로젝트_생성_성공(){
        // given
        Project sample = Project.builder().managerId(managerId).name(name).description(desc).build();
        ReflectionTestUtils.setField(sample, "id", id);

        given(projectRepository.save(any(Project.class))).willReturn(sample);

        // when
        Project project = projectService.createProject(managerId, name, desc);

        // then
        assertThat(project.getId()).isNotNull();
        assertThat(project.getManagerId()).isEqualTo(managerId);
        assertThat(project.getName()).isEqualTo(name);
        assertThat(project.getDescription()).isEqualTo(desc);
    }

}