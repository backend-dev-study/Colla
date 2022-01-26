import styled from '@emotion/styled';
import { LIGHT_GRAY } from '../../styles/color';
import { Center } from '../../styles/common';

export const Container = styled.div`
    flex-direction: column;

    ${Center}
`;

export const Wrapper = styled.div`
    display: flex;
    justify-content: center;
    align-items: start;
    width: 1300px;
    overflow-x: scroll;
    padding-top: 10px;
    padding-bottom: 10px;
`;

export const KanbanAdditional = styled.div`
    width: 300px;
    height: 50px;
    border-radius: 10px;
    background: ${LIGHT_GRAY};
    margin-left: 10px;
    margin-right: 10px;
    &:hover {
        opacity: 50%;
        cursor: pointer;
    }

    ${Center}
`;

export const KanbanAddButton = styled.img`
    width: 40px;
    height: 40px;
`;
