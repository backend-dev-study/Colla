import styled from '@emotion/styled';

import { WHITE } from '../../../styles/color';
import { Column } from '../../../styles/common';

export const TaskStatusList = styled.div`
    position: absolute;
    top: 30px;
    left: 0px;
    ${Column}
`;

export const TaskStatus = styled.button`
    width: 180px;
    border-radius: 5px;
    background: ${WHITE};
`;

export const TaskStatusName = styled.div``;
