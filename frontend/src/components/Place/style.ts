import styled from '@emotion/styled';

import { GRAY, LIGHT_GRAY, WHITE } from '../../styles/color';
import { Column } from '../../styles/common';

interface PropType {
    meetingPlace?: boolean;
}

export const Wrapper = styled.button<PropType>`
    width: 400px;
    height: 120px;
    border-radius: 20px;
    margin: 5px 10px 5px 10px;
    padding: 10px;
    background: ${({ meetingPlace }) => (meetingPlace === true ? GRAY : LIGHT_GRAY)};

    &:hover {
        opacity: 0.3;
    }
`;

export const PlaceInfo = styled.div`
    display: flex;
    justify-content: space-around;
`;

export const PlaceThumbnail = styled.div`
    width: 140px;
    height: 80px;
    border-radius: 10px;
    background: ${WHITE};
`;

export const PlaceText = styled.div`
    width: 220px;
    align-items: center;

    ${Column}
`;
export const PlaceName = styled.div`
    font-size: 24px;
`;

export const PlaceAddress = styled.div`
    font-size: 16px;
`;
