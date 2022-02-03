package kr.kro.colla.project.project.service;

import kr.kro.colla.exception.exception.project.ProjectNotFoundException;
import kr.kro.colla.exception.exception.user.UserNotManagerException;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.profile.ProjectProfileStorage;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import kr.kro.colla.project.project.presentation.dto.*;
import kr.kro.colla.project.project.service.dto.ProjectTaskResponse;
import kr.kro.colla.task.tag.domain.Tag;
import kr.kro.colla.task.tag.service.TagService;
import kr.kro.colla.task.task_tag.service.TaskTagService;
import kr.kro.colla.user.notice.domain.NoticeType;
import kr.kro.colla.user.notice.service.NoticeService;
import kr.kro.colla.user.notice.service.dto.CreateNoticeRequest;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.presentation.dto.CreateProjectRequest;
import kr.kro.colla.user.user.presentation.dto.UserProfileResponse;
import kr.kro.colla.user.user.service.UserService;
import kr.kro.colla.user_project.domain.UserProject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class ProjectService {

    private final TagService tagService;
    private final TaskTagService taskTagService;
    private final UserService userService;
    private final NoticeService noticeService;
    private final ProjectRepository projectRepository;
    private final ProjectProfileStorage projectProfileStorage;

    public Project createProject(Long managerId, CreateProjectRequest createProjectRequest) {
        String thumbnailUrl = createProjectRequest.getThumbnail() != null
                ? projectProfileStorage.upload(createProjectRequest.getThumbnail())
                : null;
        Project project = Project.builder()
                .managerId(managerId)
                .name(createProjectRequest.getName())
                .description(createProjectRequest.getDescription())
                .thumbnail(thumbnailUrl)
                .build();

        return projectRepository.save(project);
    }

    public ProjectResponse getProject(Long projectId) {
        Project project = findProjectById(projectId);
        Map<Long, User> members = new HashMap<>();
        Map<String, List<ProjectTaskResponse>> tasks = new HashMap<>();

        project.getMembers()
                .stream()
                .map(UserProject::getUser)
                .forEach(user -> members.put(user.getId(), user));

        project.getTaskStatuses()
                .stream()
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

    public List<ProjectStoryResponse> getProjectStories(Long projectId) {
        return findProjectById(projectId).getStories()
                .stream()
                .map(ProjectStoryResponse::new)
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
                .map(taskTag -> taskTag.getTag())
                .map(tag -> new ProjectTagResponse(tag))
                .collect(Collectors.toList());
    }

    public Project findProjectById(Long projectId){
        return projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);
    }

    public void inviteUserToProject(Long projectId, Long loginUserId, String memberGithubId){
        Project project  = findProjectById(projectId);
        if (project.getManagerId()!=loginUserId){
            throw new UserNotManagerException();
        }

        User user = userService.findByGithubId(memberGithubId);

        CreateNoticeRequest createNoticeRequest = CreateNoticeRequest.builder()
                .noticeType(NoticeType.INVITE_USER)
                .projectId(projectId)
                .projectName(project.getName())
                .receiverId(user.getId())
                .build();
        noticeService.createNotice(createNoticeRequest);
    }

}
