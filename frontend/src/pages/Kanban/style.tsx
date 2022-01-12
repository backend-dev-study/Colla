import styled from '@emotion/styled';
import plusIcon from '../../../public/assets/images/plus-circle.svg';
import { LIGHT_GRAY } from '../../styles/color';

export const Wrapper = styled.div`
    display: flex;
    width: 1000px;
    overflow-x: scroll;
    padding-top: 10px;
    padding-bottom: 10px;
`;

export const KanbanAdditional = styled.div`
    width: 300px;
    height: 630px;
    border-radius: 10px;
    background: ${LIGHT_GRAY};
    margin-left: 10px;
    margin-right: 10px;
    &:hover {
        opacity: 50%;
    }
`;

export const KanbanAddButton = styled.button`
    width: 300px;
    height: 630px;
    border-radius: 10px;
    background: no-repeat center url(${plusIcon});
    cursor: pointer;
`;
