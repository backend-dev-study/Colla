package kr.kro.colla.project.project.domain.repository;

import kr.kro.colla.project.project.domain.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    private Long managerId = 1L;
    private String name = "생성된 프로젝트", desc = "설명";

    @Test
    void 프로젝트_생성을_성공한다() {
        // given
        Project project = Project.builder()
                .managerId(managerId)
                .name(name)
                .description(desc)
                .build();

        // when
        Project result = projectRepository.save(project);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(name);

    }

    @Test
    void 프로젝트_생성을_실패한다() {
        // given
        Project project = Project.builder()
                .build();

        // when
        assertThatThrownBy(()-> projectRepository.save(project))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
