import styled from '@emotion/styled';

import { LIGHT_GRAY } from '../../styles/color';
import { Column } from '../../styles/common';

export const Wrapper = styled.div`
    display: flex;
    width: 80vw;
`;

export const LeftSide = styled.div`
    height: 70vh;
    margin-left: 30px;
    margin-right: 30px;
`;

export const RightSide = styled.div`
    height: 70vh;
    justify-content: space-between;
    margin-left: 30px;
    margin-right: 30px;

    ${Column}
`;

export const ProgressBar = styled.div`
    width: 35vw;
    height: 70vh;
    border-radius: 20px;
    background: ${LIGHT_GRAY};
`;

export const PieChart = styled.div`
    width: 30vw;
    height: 33vh;
    border-radius: 20px;
    background: ${LIGHT_GRAY};
`;

export const LineChart = styled.div`
    width: 30vw;
    height: 33vh;
    border-radius: 20px;
    background: ${LIGHT_GRAY};
`;
