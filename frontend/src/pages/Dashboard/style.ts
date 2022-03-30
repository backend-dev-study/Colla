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
    height: 73vh;
    justify-content: space-between;

    ${Column}
`;

export const ProgressBarContainer = styled.div`
    width: 35vw;
    height: 70vh;
    padding: 15px;
    border-radius: 20px;
    background: ${LIGHT_GRAY};
`;

export const PieChartContainer = styled.div`
    width: 33vw;
    height: 36vh;
    border-radius: 20px;
    background: ${LIGHT_GRAY};
`;

export const LineChartContainer = styled.div`
    width: 33vw;
    height: 36vh;
    border-radius: 20px;
    background: ${LIGHT_GRAY};
`;
