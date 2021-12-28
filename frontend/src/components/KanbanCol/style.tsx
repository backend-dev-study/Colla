import { css } from '@emotion/react';
import styled from '@emotion/styled';

const flexStyle = css`
    display: flex;
    justify-content: center;
`;

export const Wrapper = styled.div`
    flex-direction: column;
    margin-left: 10px;
    margin-right: 10px;

    ${flexStyle}
`;

export const KanbanStatus = styled.div`
    align-items: center;
    width: 300px;
    height: 50px;
    border-radius: 10px;
    background: rgba(196, 196, 196, 0.2);
    margin-bottom: 30px;
    font-size: 24px;
    text-align: center;

    ${flexStyle}
`;

export const KanbanIssue = styled.div`
    width: 300px;
    height: 550px;
    border-radius: 10px;
    background: rgba(196, 196, 196, 0.2);

    ${flexStyle}
`;
