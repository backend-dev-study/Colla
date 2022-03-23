import React, { FC } from 'react';
import { useLocation } from 'react-router-dom';
import { toast } from 'react-toastify';

import artIcon from '../../../public/assets/images/art.svg';
import bankIcon from '../../../public/assets/images/bank.svg';
import buildingIcon from '../../../public/assets/images/building.svg';
import cafeIcon from '../../../public/assets/images/cafe.svg';
import carIcon from '../../../public/assets/images/car.svg';
import chargingIcon from '../../../public/assets/images/charging.svg';
import childIcon from '../../../public/assets/images/child.svg';
import deleteIcon from '../../../public/assets/images/delete.png';
import educationIcon from '../../../public/assets/images/education.svg';
import homeIcon from '../../../public/assets/images/home.svg';
import hospitalIcon from '../../../public/assets/images/hospital.svg';
import hotelIcon from '../../../public/assets/images/hotel.svg';
import placeIcon from '../../../public/assets/images/location.svg';
import mapIcon from '../../../public/assets/images/map.svg';
import martIcon from '../../../public/assets/images/mart.svg';
import restaurantIcon from '../../../public/assets/images/restaurant.svg';
import schoolIcon from '../../../public/assets/images/school.svg';
import subwayIcon from '../../../public/assets/images/subway.svg';
import { createMeetingPlace, deleteMeetingPlace } from '../../apis/meeting-place';
import { MeetingPlaceType, SearchPlaceType } from '../../types/meeting-place';
import { StateType } from '../../types/project';
import { Wrapper, PlaceInfo, PlaceThumbnail, PlaceText, PlaceName, PlaceAddress, DeleteButton } from './style';

interface PropType {
    info: SearchPlaceType | MeetingPlaceType;
    meetingPlace?: boolean;
    updatePlaces?: Function;
}

const CATEGORY_TO_ICON: { [key: string]: string } = {
    대형마트: martIcon,
    편의점: martIcon,
    '어린이집,유치원': childIcon,
    학교: schoolIcon,
    학원: educationIcon,
    주차장: carIcon,
    '주유소,충전소': chargingIcon,
    지하철역: subwayIcon,
    은행: bankIcon,
    문화시설: artIcon,
    중개업소: homeIcon,
    공공기관: buildingIcon,
    관광명소: mapIcon,
    숙박: hotelIcon,
    음식점: restaurantIcon,
    카페: cafeIcon,
    병원: hospitalIcon,
    약국: hospitalIcon,
};

const Place: FC<PropType> = ({ info, meetingPlace, updatePlaces }) => {
    const { state } = useLocation<StateType>();

    const handleClick = async () => {
        if (meetingPlace) return;

        await createMeetingPlace(state.projectId, info);
        toast.success('모임 장소 추가에 성공했습니다!');
        dispatchEvent(new Event('click'));
        updatePlaces ? updatePlaces(state.projectId) : null;
    };

    const handleDeleteBtn = async () => {
        await deleteMeetingPlace((info as MeetingPlaceType).id);
        window.location.replace(`/meeting-place`);
    };

    return (
        <Wrapper onClick={handleClick} meetingPlace={meetingPlace}>
            {meetingPlace ? <DeleteButton src={deleteIcon} onClick={handleDeleteBtn} /> : null}
            <PlaceInfo>
                <PlaceThumbnail src={CATEGORY_TO_ICON[info.category] ? CATEGORY_TO_ICON[info.category] : placeIcon} />
                <PlaceText>
                    <PlaceName>{info.name}</PlaceName>
                    <PlaceAddress>{info.address}</PlaceAddress>
                </PlaceText>
            </PlaceInfo>
        </Wrapper>
    );
};

export default Place;
