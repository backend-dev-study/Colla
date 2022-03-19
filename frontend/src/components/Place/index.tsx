import React, { FC } from 'react';
import { useLocation } from 'react-router-dom';
import { toast } from 'react-toastify';

import DeleteIconImg from '../../../public/assets/images/delete.png';
import { createMeetingPlace, deleteMeetingPlace } from '../../apis/meeting-place';
import { MeetingPlaceType, SearchPlaceType } from '../../types/meeting-place';
import { StateType } from '../../types/project';
import { DeleteButton, Wrapper } from './style';

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
            {info.image ? <img src={info.image} /> : null}
            {meetingPlace ? <DeleteButton src={DeleteIconImg} onClick={handleDeleteBtn} /> : null}
            <div>{info.name}</div>
            <div>{info.address}</div>
        </Wrapper>
    );
};

export default Place;
