package kr.kro.colla.user.user.presentation.dto;

import kr.kro.colla.user.user.service.dto.UserProjectResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class UserProjectResponse {

    private Long id;

    private Long managerId;

    private String name;

    private String description;

    private String thumbnail;

    private UserProjectResponse(UserProjectResponseDto userProjectResponseDto) {
        this.id = userProjectResponseDto.getId();
        this.managerId = userProjectResponseDto.getManagerId();
        this.name = userProjectResponseDto.getName();
        this.description = userProjectResponseDto.getDescription();
        this.thumbnail = userProjectResponseDto.getThumbnail();
    }

    public static List<UserProjectResponse> of(List<UserProjectResponseDto> userProjectResponseDtoList) {
        return userProjectResponseDtoList.stream()
                .map(project -> new UserProjectResponse(project))
                .collect(Collectors.toList());
    }

}
