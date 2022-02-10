import styled from '@emotion/styled';
import { GREEN, LIGHT_GRAY } from '../../styles/color';
import { WidthCenter } from '../../styles/common';

export const Wrapper = styled.div`
    flex-direction: column;
    margin-left: 10px;
    margin-right: 10px;

    ${WidthCenter}
`;

export const KanbanStatus = styled.div`
    position: relative;
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
    border-radius: 10px;
    background: ${LIGHT_GRAY};
    flex-direction: column;
    align-items: center;
`;

export const AddTaskButton = styled.div`
    display: flex;
    flex-direction: row;
    align-items: center;
    width: 250px;
    height: 20px;
    border: 3px solid ${GREEN};
    border-radius: 10px;
    margin: 10px 10px;
    padding: 15px;
    color: #a9a9a9;
    cursor: pointer;
`;

export const PlusIcon = styled.img`
    width: 50px;
    height: 24px;
    margin-left: -15px;
`;

export const DeleteStatusButton = styled.button`
    position: absolute;
    right: 0px;
    border-radius: 10px;
`;

export const DeleteIcon = styled.img`
    display: flex;
    align-items: center;
    width: 50px;
    height: 24px;
`;
