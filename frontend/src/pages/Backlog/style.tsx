import styled from '@emotion/styled';
import { LIGHT_GRAY } from '../../styles/color';
import { Column } from '../../styles/common';

export const Container = styled.div`
    align-items: center;

    ${Column}
`;

export const Wrapper = styled.div`
    width: 1300px;
    height: 500px;
    border-radius: 20px;
    overflow-y: scroll;
    background: ${LIGHT_GRAY};
    align-items: end;

    &::-webkit-scrollbar {
        width: 8px;
    }

    &::-webkit-scrollbar-thumb {
        background: #bbbbbb;
        border-radius: 10px;
    }

    ${Column}
`;

export const TaskModalContainer = styled.div`
    position: absolute;
    top: 100px;
    left: 300px;
`;
