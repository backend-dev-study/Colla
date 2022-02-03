package kr.kro.colla.task.task_tag.domain.repository;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.task.tag.domain.Tag;
import kr.kro.colla.task.task_tag.domain.TaskTag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class TaskTagRepositoryTest {

    @Autowired
    private TaskTagRepository taskTagRepository;

    @Test
    void 프로젝트_태그를_생성한다() {
        // given
        Project project = Project.builder()
                .name("project name")
                .description("project description")
                .build();
        Tag tag = new Tag("backend");
        TaskTag taskTag = TaskTag.builder()
                .project(project)
                .tag(tag)
                .build();

        // when
        TaskTag result = taskTagRepository.save(taskTag);

        // then
        assertThat(result.getProject()).isNotNull();
        assertThat(result.getTag()).isNotNull();
    }

}
