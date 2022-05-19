package kr.kro.colla.project.project.service;

import kr.kro.colla.exception.exception.project.ProjectNotFoundException;
import kr.kro.colla.exception.exception.project.task_status.TaskStatusAlreadyExistException;
import kr.kro.colla.exception.exception.user.UserNotManagerException;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.profile.ProjectProfileStorage;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import kr.kro.colla.project.project.presentation.dto.*;
import kr.kro.colla.project.project.service.dto.ProjectTaskResponse;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.task.tag.domain.Tag;
import kr.kro.colla.task.tag.service.TagService;
import kr.kro.colla.task.task_tag.domain.TaskTag;
import kr.kro.colla.task.task_tag.service.TaskTagService;
import kr.kro.colla.user.notice.service.NoticeService;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.presentation.dto.CreateProjectRequest;
import kr.kro.colla.user.user.presentation.dto.UserProfileResponse;
import kr.kro.colla.user.user.service.UserService;
import kr.kro.colla.user_project.domain.UserProject;
import kr.kro.colla.user_project.service.UserProjectService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectProfileStorage projectProfileStorage;

    private final TagService tagService;
    private final TaskTagService taskTagService;
    private final NoticeService noticeService;
    private final UserService userService;
    private final UserProjectService userProjectService;

    public Project createProject(Long managerId, CreateProjectRequest createProjectRequest) {
        User user = userService.findUserById(managerId);
        String thumbnailUrl = createProjectRequest.getThumbnail() != null
                ? projectProfileStorage.upload(createProjectRequest.getThumbnail())
                : null;
        Project project = Project.builder()
                .managerId(managerId)
                .name(createProjectRequest.getName())
                .description(createProjectRequest.getDescription())
                .thumbnail(thumbnailUrl)
                .build();

        Project createdProject = projectRepository.save(project);
        userProjectService.joinProject(user, createdProject);

        return createdProject;
    }

    @Cacheable(
            key = "#projectId",
            value = "Project",
            condition = "#projectId != null",
            unless = "#result == null"
    )
    public ProjectResponse getProjectWithTasks(Long projectId) {
        Project project = findProjectById(projectId);
        Map<Long, User> members = new HashMap<>();
        Map<String, List<ProjectTaskResponse>> tasks = new HashMap<>();

        project.getMembers()
                .stream()
                .map(UserProject::getUser)
                .forEach(user -> members.put(user.getId(), user));

        project.getTaskStatuses()
                .forEach(taskStatus -> {
                    List<ProjectTaskResponse> taskList = taskStatus.getTasks()
                            .stream()
                            .map(task -> task.getManagerId() != null
                                    ? new ProjectTaskResponse(task, members.get(task.getManagerId()))
                                    : new ProjectTaskResponse(task))
                            .collect(Collectors.toList());
                    tasks.put(taskStatus.getName(), taskList);
                });


        return ProjectResponse.builder()
                .id(project.getId())
                .managerId(project.getManagerId())
                .name(project.getName())
                .description(project.getDescription())
                .thumbnail(project.getThumbnail())
                .members(project.getMembers()
                        .stream()
                        .map(userProject -> new UserProfileResponse(userProject.getUser()))
                        .collect(Collectors.toList()))
                .tasks(tasks)
                .build();
    }

    public List<ProjectStorySimpleResponse> getProjectStories(Long projectId) {
        return findProjectById(projectId).getStories()
                .stream()
                .map(ProjectStorySimpleResponse::new)
                .collect(Collectors.toList());
    }

    public List<ProjectMemberResponse> getProjectMembers(Long projectId) {
        return findProjectById(projectId).getMembers()
                .stream()
                .map(userProject -> new ProjectMemberResponse(userProject.getUser()))
                .collect(Collectors.toList());
    }

    public Tag createTag(Long projectId, CreateTagRequest createTagRequest) {
        Project project = findProjectById(projectId);
        Tag tag = tagService.createTagIfNotExist(createTagRequest.getName());
        taskTagService.addNewTag(project, tag);

        return tag;
    }

    public List<ProjectTagResponse> getProjectTags(Long projectId) {
        return findProjectById(projectId).getTaskTags()
                .stream()
                .map(TaskTag::getTag)
                .map(ProjectTagResponse::new)
                .collect(Collectors.toList());
    }

    public void inviteUserToProject(Long projectId, Long loginUserId, String memberGithubId) {
        Project project = findProjectById(projectId);
        if (!Objects.equals(project.getManagerId(), loginUserId)){
            throw new UserNotManagerException();
        }

        User user = userService.findByGithubId(memberGithubId);
        noticeService.createNotice(project, user);
    }

    public ProjectMemberResponse decideInvitation(Long projectId, Long loginUserId, ProjectMemberDecision projectMemberDecision) {
        Project project = findProjectById(projectId);
        User user = userService.findUserById(loginUserId);

        noticeService.checkNotice(projectMemberDecision.getNoticeId(), user);
        if (projectMemberDecision.isAccept()) {
            UserProject userProject = userProjectService.joinProject(user, project);
            return new ProjectMemberResponse(userProject.getUser());
        }
        return null;
    }

    public TaskStatus createTaskStatus(Long projectId, String name) {
        Project project = findProjectById(projectId);

        Optional<TaskStatus> exist = project.getTaskStatuses()
                .stream()
                .filter(taskStatus -> taskStatus.getName().equals(name))
                .findFirst();
        if (exist.isPresent()) {
            throw new TaskStatusAlreadyExistException();
        }

        TaskStatus taskStatus = new TaskStatus(name);
        project.addStatus(taskStatus);
        return taskStatus;
    }

    public List<ProjectTaskStatusResponse> getTaskStatuses(Long projectId) {
        Project project = findProjectById(projectId);

        return project.getTaskStatuses()
                .stream()
                .map(ProjectTaskStatusResponse::new)
                .collect(Collectors.toList());
    }

    public Project findProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);
    }

    public Project initializeProjectInfo(Long projectId) {
        Project project = findProjectById(projectId);
        Hibernate.initialize(project.getMembers());
        Hibernate.initialize(project.getTaskStatuses());

        return project;
    }

}
