package kr.kro.colla.meeting_place.meeting_place.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class SearchByMapBoundaryRequest {

    @NotNull
    private Double minLng;

    @NotNull
    private Double maxLng;

    @NotNull
    private Double minLat;

    @NotNull
    private Double maxLat;

}
