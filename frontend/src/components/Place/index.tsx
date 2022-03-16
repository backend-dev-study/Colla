import React, { FC } from 'react';
import { useLocation } from 'react-router-dom';
import { toast } from 'react-toastify';

import { createMeetingPlace } from '../../apis/meeting-place';
import { SearchPlaceType } from '../../types/meeting-place';
import { StateType } from '../../types/project';
import { Wrapper } from './style';

interface PropType {
    info: SearchPlaceType;
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

    return (
        <Wrapper onClick={handleClick} meetingPlace={meetingPlace}>
            {info.image ? <img src={info.image} /> : null}
            <div>{info.name}</div>
            <div>{info.address}</div>
        </Wrapper>
    );
};

export default Place;
