import styled from '@emotion/styled';

import { WHITE } from '../../../../styles/color';
import { LiftUp } from '../../../../styles/common';

export const TaskContainer = styled.div`
    display: flex;
    flex-direction: column;
`;

export const Title = styled.div`
    display: flex;
    margin: 40px 0 0 40px;

    span {
        margin-top: 12px;
        min-width: 80px;
    }
`;

export const TitleInput = styled.input`
    border: none;
    width: 375px;
    height: 40px;
    border-radius: 10px;
    padding-left: 10px;
    font-size: 15px;
`;

export const TaskComponent = styled.div`
    display: flex;
    margin: 20px 0 0 40px;

    span {
        min-width: 80px;
        margin-top: 6px;
    }
`;

export const PreTask = styled.div`
    display: flex;
    width: 375px;
    align-items: center;
`;

export const DeleteButton = styled.img`
    width: 15px;
    height: 15px;
    margin-right: 12px;
    cursor: pointer;
`;

export const DescriptionArea = styled.textarea`
    font-size: 15px;
    border-radius: 10px;
    padding: 10px 0 0 10px;
`;

export const DropDown = styled.div`
    display: flex;
    align-items: center;
    width: 375px;
    min-height: 30px;
    border-radius: 10px;
    padding: 5px 0 5px 10px;
    background-color: ${WHITE};
    cursor: pointer;
`;

export const AddButton = styled.div`
    margin-top: 5px;
    margin-left: 10px;
    cursor: pointer;

    ${LiftUp}
`;
