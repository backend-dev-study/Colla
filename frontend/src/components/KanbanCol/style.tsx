import { css } from '@emotion/react';
import styled from '@emotion/styled';

const flexStyle = css`
    display: flex;
    justify-content: center;
`;

export const Wrapper = styled.div`
    flex-direction: column;
    width: 300px;
    margin-left: 10px;
    margin-right: 10px;

    ${flexStyle}
`;

export const KanbanStatus = styled.div`
    align-items: center;
    height: 50px;
    border-radius: 10px;
    background-color: #eeeeee;

    margin-bottom: 30px;
    font-size: 24px;
    text-align: center;

    ${flexStyle}
`;

export const KanbanIssue = styled.div`
    height: 550px;
    border-radius: 10px;
    background-color: #eeeeee;

    ${flexStyle}
`;
