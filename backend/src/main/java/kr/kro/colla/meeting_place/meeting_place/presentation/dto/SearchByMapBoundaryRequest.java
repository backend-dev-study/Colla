package kr.kro.colla.meeting_place.meeting_place.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SearchByMapBoundaryRequest {

    @NotNull
    private Double minLongitude;

    @NotNull
    private Double maxLongitude;

    @NotNull
    private Double minLatitude;

    @NotNull
    private Double maxLatitude;

}
