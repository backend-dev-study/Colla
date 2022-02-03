package kr.kro.colla.project.project.service;

import kr.kro.colla.common.fixture.FileProvider;
import kr.kro.colla.exception.exception.user.UserNotManagerException;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.profile.ProjectProfileStorage;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import kr.kro.colla.project.project.presentation.dto.*;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.story.domain.Story;
import kr.kro.colla.task.tag.domain.Tag;
import kr.kro.colla.task.tag.service.TagService;
import kr.kro.colla.task.task_tag.domain.TaskTag;
import kr.kro.colla.task.task_tag.service.TaskTagService;
import kr.kro.colla.user.notice.domain.Notice;
import kr.kro.colla.user.notice.domain.NoticeType;
import kr.kro.colla.user.notice.service.NoticeService;
import kr.kro.colla.user.notice.service.dto.CreateNoticeRequest;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.presentation.dto.CreateProjectRequest;
import kr.kro.colla.user.user.service.UserService;
import kr.kro.colla.user_project.domain.UserProject;
import kr.kro.colla.user_project.service.UserProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectProfileStorage projectProfileStorage;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TagService tagService;

    @Mock
    private TaskTagService taskTagService;

    @Mock
    private UserService userService;

    @Mock
    private NoticeService noticeService;

    @Mock
    private UserProjectService userProjectService;

    @InjectMocks
    private ProjectService projectService;

    private Long id = 1L, managerId = 1L;
    private String name = "프로젝트 이름", desc = "프로젝트 설명";

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

    @Test
    void projectId에_해당하는_프로젝트를_조회한다() {
        // given
        String fileName = "thumbnail.png";
        MultipartFile thumbnail1 = FileProvider.getTestMultipartFile(fileName);
        User user = User.builder()
                .name("kykapple")
                .githubId("kykapple")
                .avatar("github_content")
                .build();
        Project project = Project.builder()
                .managerId(managerId)
                .name(name)
                .description(desc)
                .thumbnail(FileProvider.extractImageUrl(thumbnail1))
                .build();
        ReflectionTestUtils.setField(project, "taskStatuses", List.of(
                new TaskStatus("To Do"),
                new TaskStatus("In Progress"),
                new TaskStatus("Done")
        ));
        ReflectionTestUtils.setField(project, "members", List.of(new UserProject(user, project)));

        given(projectRepository.findById(eq(id)))
                .willReturn(Optional.of(project));

        // when
        ProjectResponse result = projectService.getProject(id);

        // then
        assertThat(result.getName()).isEqualTo(project.getName());
        assertThat(result.getDescription()).isEqualTo(project.getDescription());
        assertThat(result.getMembers().size()).isOne();
        assertThat(result.getMembers().get(0).getGithubId()).isEqualTo(user.getGithubId());
        assertThat(result.getTasks().size()).isEqualTo(3);
        assertThat(result.getTasks().get("Done")).isEmpty();
    }

    @Test
    void 프로젝트의_스토리를_조회한다() {
        // given
        String fileName = "thumbnail.png";
        MultipartFile thumbnail1 = FileProvider.getTestMultipartFile(fileName);
        Project project = Project.builder()
                .managerId(managerId)
                .name(name)
                .description(desc)
                .thumbnail(FileProvider.extractImageUrl(thumbnail1))
                .build();
        Story story = Story.builder()
                .title("story title")
                .preStories("[]")
                .build();
        ReflectionTestUtils.setField(project, "stories", List.of(story));

        given(projectRepository.findById(eq(id)))
                .willReturn(Optional.of(project));

        // when
        List<ProjectStoryResponse> result = projectService.getProjectStories(id);

        // then
        ProjectStoryResponse response = result.get(0);
        assertThat(result.size()).isEqualTo(1);
        assertThat(response.getTitle()).isEqualTo(story.getTitle());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void 프로젝트의_멤버를_조회한다() {
        // given
        Project project = Project.builder()
                .managerId(managerId)
                .name(name)
                .description(desc)
                .build();
        User user = User.builder()
                .name("yeongkee")
                .githubId("kykapple")
                .avatar("github_content")
                .build();
        UserProject userProject = UserProject.builder()
                .project(project)
                .user(user)
                .build();
        ReflectionTestUtils.setField(project, "members", List.of(userProject));
        ReflectionTestUtils.setField(userProject, "user", user);

        given(projectRepository.findById(id))
                .willReturn(Optional.of(project));

        // when
        List<ProjectMemberResponse> result = projectService.getProjectMembers(id);

        // then
        ProjectMemberResponse response = result.get(0);
        assertThat(result.size()).isEqualTo(1);
        assertThat(response.getName()).isEqualTo(user.getName());
        assertThat(response.getAvatar()).isEqualTo(user.getAvatar());
        verify(projectRepository, times(1)).findById(1L);
    }

    @Test
    void 프로젝트에서_사용할_태그를_등록한다() {
        // given
        String tagName = "backend";
        CreateTagRequest createTagRequest = new CreateTagRequest(tagName);
        Tag tag = new Tag(tagName);
        Project project = Project.builder()
                .managerId(managerId)
                .name(name)
                .description(desc)
                .build();

        given(projectRepository.findById(id))
                .willReturn(Optional.of(project));
        given(tagService.createTagIfNotExist(tagName))
                .willReturn(tag);

        // when
        Tag result = projectService.createTag(id, createTagRequest);

        // then
        assertThat(result.getName()).isEqualTo(tag.getName());
        verify(projectRepository, times(1)).findById(id);
        verify(tagService, times(1)).createTagIfNotExist(any(String.class));
        verify(taskTagService, times(1)).addNewTag(any(Project.class), any(Tag.class));
    }

    @Test
    void 프로젝트에_등록되어_있는_테스크_태그들을_조회한다() {
        // given
        Project project = Project.builder()
                .managerId(managerId)
                .name(name)
                .description(desc)
                .build();
        Tag tag = new Tag("backend");
        TaskTag taskTag = TaskTag.builder()
                .project(project)
                .tag(tag)
                .build();
        ReflectionTestUtils.setField(project, "taskTags", List.of(taskTag));
        ReflectionTestUtils.setField(taskTag, "tag", tag);

        given(projectRepository.findById(id))
                .willReturn(Optional.of(project));

        // when
        List<ProjectTagResponse> result = projectService.getProjectTags(id);

        // then
        ProjectTagResponse projectTagResponse = result.get(0);
        assertThat(result.size()).isEqualTo(1);
        assertThat(projectTagResponse.getName()).isEqualTo(tag.getName());
    }

    @Test
    void 프로젝트_멤버_초대에_성공한다(){
        // given
        Long loginId = 987234L, projectId = 84234L;
        String githubId = "random_github_id";
        User user = User.builder()
                .name("subin")
                .avatar("github_url")
                .githubId(githubId)
                .build();
        Project project = Project.builder()
                .name("random_project_name")
                .managerId(loginId)
                .build();

        given(projectRepository.findById(projectId))
                .willReturn(Optional.of(project));
        given(userService.findByGithubId(githubId))
                .willReturn(user);
        // when
        projectService.inviteUserToProject(projectId, loginId, githubId);

        // then
        verify(userService, times(1)).findByGithubId(githubId);
        verify(noticeService, times(1)).createNotice(any(CreateNoticeRequest.class));

    }

    @Test
    void 프로젝트_초대에_매니저_권한이_아니여서_실패한다(){
        Long loginId = 987234L, projectId = 84234L, managerId=234234L;
        String githubId = "random_github_id";
        User user = User.builder()
                .name("subin")
                .avatar("github_url")
                .githubId(githubId)
                .build();
        Project project = Project.builder()
                .name("random_project_name")
                .managerId(managerId)
                .build();

        given(projectRepository.findById(projectId))
                .willReturn(Optional.of(project));
        // when
        assertThatThrownBy(()->{
          projectService.inviteUserToProject(projectId, loginId, githubId);
        }).isInstanceOf(UserNotManagerException.class);
    }

    @Test
    void 프로젝트_초대_수락에_성공한다() {
        // given
        Long loginId = 2455L, projectId = 23414L, noticeId = 43524L;
        User user = User.builder()
                .name("name__")
                .avatar("avatar__")
                .githubId("github__")
                .build();
        ReflectionTestUtils.setField(user, "id", loginId);
        Project project = Project.builder()
                .name("project__")
                .managerId(230492L)
                .build();
        ReflectionTestUtils.setField(project, "id", projectId);
        UserProject userProject = UserProject.builder()
                        .project(project)
                        .user(user)
                        .build();
        Notice notice = Notice.builder()
                .noticeType(NoticeType.INVITE_USER)
                .projectId(project.getId())
                .projectName(project.getName())
                .build();
        ReflectionTestUtils.setField(notice, "id", noticeId);

        given(userService.findUserById(loginId))
                .willReturn(user);
        given(projectRepository.findById(projectId))
                .willReturn(Optional.of(project));
        given(userProjectService.joinProject(any(User.class), any(Project.class)))
                .willReturn(userProject);
        given(noticeService.findById(noticeId))
                .willReturn(notice);
        // when
        Optional<ProjectMemberResponse> result = projectService.handleInvitationDecision(projectId, loginId, new ProjectMemberDecision(true, noticeId));
        // then
        assertThat(result.isPresent()).isEqualTo(true);
        ProjectMemberResponse response = result.get();
        assertThat(response.getId()).isEqualTo(user.getId());
        assertThat(response.getName()).isEqualTo(user.getName());
        assertThat(response.getGithubId()).isEqualTo(user.getGithubId());
        verify(userProjectService, times(1)).joinProject(any(User.class), any(Project.class));
        assertThat(notice.getIsChecked()).isEqualTo(true);
    }

    @Test
    void 프로젝트_초대_거절에_성공한다() {
        // given
        Long loginId = 2455L, projectId = 23414L, noticeId = 74123L;
        Project project = Project.builder()
                .name("projectName")
                .managerId(194910L)
                .build();
        ReflectionTestUtils.setField(project, "id", projectId);
        Notice notice = Notice.builder()
                .noticeType(NoticeType.INVITE_USER)
                .projectId(project.getId())
                .projectName(project.getName())
                .build();
        ReflectionTestUtils.setField(notice, "id", noticeId);

        given(projectRepository.findById(projectId))
                .willReturn(Optional.of(project));
        given(noticeService.findById(noticeId))
                .willReturn(notice);
        // when
        projectService.handleInvitationDecision(projectId, loginId, new ProjectMemberDecision(false, noticeId));
        // then
        verify(userProjectService, times(0)).joinProject(any(), any());
        assertThat(notice.getIsChecked()).isEqualTo(true);
    }
}
