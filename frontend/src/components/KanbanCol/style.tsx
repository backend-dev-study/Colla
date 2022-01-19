import styled from '@emotion/styled';
import { LIGHT_GRAY } from '../../styles/color';
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
    background: ${LIGHT_GRAY};
    margin-bottom: 30px;
    font-size: 24px;
    text-align: center;

    ${WidthCenter}
`;

export const KanbanIssue = styled.div`
    display: flex;
    width: 300px;
    height: 550px;
    border-radius: 10px;
    background: ${LIGHT_GRAY};
    flex-direction: column;
`;
