package kr.kro.colla.meeting_place.meeting_place.presentation.dto;

import kr.kro.colla.meeting_place.meeting_place.domain.MeetingPlace;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MeetingPlaceResponse {
    private Long id;

    private String name;

    private String image;

    private Double longitude;

    private Double latitude;

    private String address;

    private String category;

    public MeetingPlaceResponse(MeetingPlace place) {
        this.id = place.getId();
        this.name = place.getName();
        this.image = place.getImage();
        this.longitude = place.getLongitude();
        this.latitude = place.getLatitude();
        this.address = place.getAddress();
        this.category = place.getCategory();
    }
}
