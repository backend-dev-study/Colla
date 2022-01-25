import { css } from '@emotion/react';
import styled from '@emotion/styled';
import { GRAY } from '../../../styles/color';
import { LiftUp } from '../../../styles/common';

export const ModalContainer = styled.div`
    display: flex;
    flex-direction: column;
    width: 880px;
    min-height: 430px;
    margin-top: 70px;
    border-radius: 20px;
    background-color: #f1f1f1;
    box-shadow: 0 4px 4px rgba(0, 0, 0, 0.1), 0 4px 20px rgba(0, 0, 0, 0.1);
`;

export const Container = styled.div`
    display: flex;
    flex-direction: row;
`;

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

export const DescriptionArea = styled.textarea`
    font-size: 15px;
    border: none;
    resize: none;
    outline: none;
    border-radius: 10px;
    padding: 10px 0 0 10px;
`;

export const DropDown = styled.div`
    display: flex;
    width: 375px;
    height: 30px;
    border-radius: 10px;
    padding-left: 10px;
    background-color: #fff;
    cursor: pointer;
`;

export const DownIcon = styled.img`
    width: 15px;
    height: 15px;
    margin: 6px 12px 0 auto;
`;

export const AddButton = styled.div`
    margin-top: 5px;
    margin-left: 10px;
    cursor: pointer;

    ${LiftUp}
`;

export const DetailContainer = styled.div`
    display: flex;
    flex-direction: column;
    width: 260px;
    height: 330px;
    margin: 30px 0 0 40px;
    border-radius: 20px;
    background-color: ${GRAY};
`;

export const DetailComponent = styled.div`
    display: flex;
    flex-direction: column;
    margin: 20px 0 0 20px;
`;

export const MemberList = styled.div`
    display: flex;
    width: 200px;
    height: 30px;
    border: 1px solid #000;
    border-radius: 10px;
    margin-top: 10px;
    padding-left: 10px;
    background-color: transparent;
`;

export const Status = styled.div`
    margin-top: 10px;
`;

export const Priority = styled.div`
    display: flex;
    justify-content: space-between;
    width: 200px;
    margin-top: 10px;

    span {
        cursor: pointer;
        ${LiftUp}
    }
`;

export const ButtonContainer = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    margin-top: 20px;
`;

const Button = css`
    width: 50px;
    height: 50px;
    font-size: 18px;
    cursor: pointer;
    text-decoration: underline;

    ${LiftUp}
`;

export const CancelButton = styled.div`
    margin-left: 40px;
    color: #ff0000;

    ${Button}
`;

export const CompleteButton = styled.div`
    margin-right: 19px;

    ${Button};
`;
