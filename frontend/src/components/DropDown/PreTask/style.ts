import styled from '@emotion/styled';
import { GRAY } from '../../../styles/color';

export const TaskList = styled.div`
    position: absolute;
    display: flex;
    flex-direction: column;
    width: 375px;
    height: 300px;
    top: 430px;
    left: 120px;
    border: 1px solid ${GRAY};
    border-radius: 10px;
    padding-left: 10px;
    background-color: #fff;
    overflow-y: scroll;

    &::-webkit-scrollbar {
        width: 8px;
    }

    &::-webkit-scrollbar-thumb {
        background: #bbbbbb;
        border-radius: 10px;
    }
`;

export const Task = styled.div`
    display: flex;
    width: 337px;
    justify-content: space-between;
    padding: 10px;
    margin-right: 10px;
    border-bottom: 1px solid #000;

    :last-child {
        border: none;
    }

    :hover {
        background-color: #f1f1f1;
    }
`;

export const TaskTitle = styled.div`
    max-width: 260px;
`;

export const Priority = styled.div``;
