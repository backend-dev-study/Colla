import React, { FC } from 'react';
import { useLocation } from 'react-router-dom';
import { toast } from 'react-toastify';

import placeIcon from '../../../public/assets/images/location.svg';
import { StateType } from '../../types/project';
import { Wrapper, PlaceInfo, PlaceThumbnail, PlaceText, PlaceName, PlaceAddress, DeleteButton } from './style';
import DeleteIconImg from '../../../public/assets/images/delete.png';
import { createMeetingPlace, deleteMeetingPlace } from '../../apis/meeting-place';
import { MeetingPlaceType, SearchPlaceType } from '../../types/meeting-place';

interface PropType {
    info: SearchPlaceType | MeetingPlaceType;
    meetingPlace?: boolean;
    updatePlaces?: Function;
}

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
            {meetingPlace ? <DeleteButton src={DeleteIconImg} onClick={handleDeleteBtn} /> : null}
            <PlaceInfo>
                <PlaceThumbnail src={placeIcon} />
                <PlaceText>
                    <PlaceName>{info.name}</PlaceName>
                    <PlaceAddress>{info.address}</PlaceAddress>
                </PlaceText>
            </PlaceInfo>
        </Wrapper>
    );
};

export default Place;
