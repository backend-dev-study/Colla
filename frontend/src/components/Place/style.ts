import styled from '@emotion/styled';

import { GRAY, LIGHT_GRAY } from '../../styles/color';

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
