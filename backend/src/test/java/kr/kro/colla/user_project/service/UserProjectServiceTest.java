package kr.kro.colla.user_project.service;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user_project.domain.UserProject;
import kr.kro.colla.user_project.domain.repository.UserProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserProjectServiceTest {
    @Mock
    private UserProjectRepository userProjectRepository;

    @InjectMocks
    private UserProjectService userProjectService;

    @Test
    void 프로젝트의_사용자_추가에_성공한다(){
        User user = User.builder()
                .githubId("binimini")
                .name("subin")
                .avatar("githubcontent").build();
        Project project = Project.builder()
                .name("project_name")
                .managerId(345L)
                .build();
        UserProject userProject = UserProject.builder()
                .user(user)
                .project(project).build();
        ReflectionTestUtils.setField(userProject, "id", 234L);
        // given
        given(userProjectRepository.save(any(UserProject.class))).willReturn(userProject);

        // when
        UserProject result = userProjectService.addUserToProject(user, project);

        // then
        verify(userProjectRepository, times(1)).save(any(UserProject.class));
        assertThat(result.getId()).isEqualTo(234);
        assertThat(result.getUser()).isEqualTo(user);
        assertThat(result.getProject()).isEqualTo(project);
    }
}