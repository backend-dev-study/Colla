import styled from '@emotion/styled';
import plusIcon from '../../assets/plus-circle.svg';

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
    background: rgba(196, 196, 196, 0.2);
    margin-left: 10px;
    margin-right: 10px;
    &:hover {
        background: rgba(196, 196, 196, 0.35);
    }
`;

export const KanbanAddButton = styled.button`
    width: 300px;
    height: 630px;
    border-radius: 10px;
    background: no-repeat center url(${plusIcon});
    cursor: pointer;
`;
