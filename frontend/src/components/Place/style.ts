import styled from '@emotion/styled';

import { BLACK, GRAY, LIGHT_GRAY } from '../../styles/color';
import { LiftUp } from '../../styles/common';

interface PropType {
    meetingPlace?: boolean;
}

export const Wrapper = styled.button<PropType>`
    position: relative;
    width: 400px;
    height: 120px;
    border-radius: 20px;
    margin: 5px 10px 5px 10px;
    padding: 10px;
    background: ${({ meetingPlace }) => (meetingPlace === true ? GRAY : LIGHT_GRAY)};

    &:hover {
        opacity: 1;
        ${LiftUp}
    }
`;

export const DeleteButton = styled.img`
    position: absolute;
    width: 15px;
    height: 15px;
    margin: -20px 0 0 165px;

    &:hover {
        border-bottom: 2px solid ${BLACK};
    }
`;
