import styled from '@emotion/styled';

import { GRAY, LIGHT_GRAY } from '../../styles/color';
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
`;

export const PlaceThumbnail = styled.img`
    width: 50px;
    height: 50px;
    margin-left: 20px;
    margin-right: 20px;
`;

export const PlaceText = styled.div`
    width: 300px;
    align-items: flex-start;

    ${Column}
`;
export const PlaceName = styled.div`
    font-size: 24px;
`;

export const PlaceAddress = styled.div`
    font-size: 16px;
`;
