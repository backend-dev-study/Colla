import styled from '@emotion/styled';
import { WidthCenter } from '../../styles/common';

export const Wrapper = styled.div`
    flex-direction: column;
    margin-left: 10px;
    margin-right: 10px;

    ${WidthCenter}
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

    ${WidthCenter}
`;

export const KanbanIssue = styled.div`
    width: 300px;
    height: 550px;
    border-radius: 10px;
    background: rgba(196, 196, 196, 0.2);

    ${WidthCenter}
`;
