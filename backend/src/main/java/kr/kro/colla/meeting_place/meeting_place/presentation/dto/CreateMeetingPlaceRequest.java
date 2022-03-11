package kr.kro.colla.meeting_place.meeting_place.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class CreateMeetingPlaceRequest {
    @NotNull
    private String name;

    private String image;

    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;

    @NotNull
    private String address;
}
