import styled from '@emotion/styled';
import { LiftUp } from '../../../styles/common';

export const Container = styled.div`
    display: flex;
    justify-content: space-between;
    width: 200px;
    margin-top: 10px;
`;

export const Weight = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    width: 24px;
    height: 24px;
    cursor: pointer;
    ${LiftUp}

    &.selected {
        border: 1px solid #000;
        border-radius: 50px;
    }
`;
