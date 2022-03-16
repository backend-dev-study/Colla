import React, { FC } from 'react';
import { useLocation } from 'react-router-dom';
import { toast } from 'react-toastify';

import { createMeetingPlace } from '../../apis/meeting-place';
import { SearchPlaceType } from '../../types/meeting-place';
import { StateType } from '../../types/project';
import { Wrapper } from './style';

interface PropType {
    info: SearchPlaceType;
}

const Place: FC<PropType> = ({ info }) => {
    const { state } = useLocation<StateType>();

    const handleClick = async () => {
        await createMeetingPlace(state.projectId, info);
        toast.success('모임 장소 추가에 성공했습니다!');
        dispatchEvent(new Event('click'));
    };

    return (
        <Wrapper onClick={handleClick}>
            {info.image ? <img src={info.image} /> : null}
            <div>{info.name}</div>
            <div>{info.address}</div>
        </Wrapper>
    );
};

export default Place;
