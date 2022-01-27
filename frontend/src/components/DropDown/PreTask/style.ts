import { css } from '@emotion/react';
import styled from '@emotion/styled';
import { GRAY } from '../../../styles/color';

export const List = css`
    position: absolute;
    display: flex;
    flex-direction: column;
    width: 375px;
    max-height: 300px;
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

export const TaskList = styled.div`
    top: 450px;
    left: 120px;

    ${List}
`;

export const Work = css`
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
        cursor: pointer;
    }
`;

export const Task = styled.div`
    ${Work}
`;

export const Title = css`
    max-width: 260px;
`;

export const TaskTitle = styled.div`
    ${Title}
`;

export const Priority = styled.div``;
