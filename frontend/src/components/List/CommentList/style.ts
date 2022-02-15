import styled from '@emotion/styled';
import { LiftUp } from '../../../styles/common';

export const Container = styled.div`
    display: flex;
    flex-direction: column;
    width: 530px;
    margin-left: 3px;
    max-height: 250px;
    overflow-y: scroll;

    &::-webkit-scrollbar {
        width: 8px;
    }

    &::-webkit-scrollbar-thumb {
        background: #bbbbbb;
        border-radius: 10px;
    }
`;

export const SuperCommentContainer = styled.div`
    display: flex;
    flex-direction: column;
    margin-top: 15px;
`;

export const SuperComment = styled.div`
    display: flex;
    flex-direction: row;
    margin-bottom: 7px;
`;

export const SubCommentContainer = styled.div`
    display: flex;
    flex-direction: column;
    margin: 0 0 7px 45px;
`;

export const SubComment = styled.div`
    margin: 10px 0;
`;

export const SubCommentInputContainer = styled.div`
    display: flex;
    flex-direction: row;
    margin-top: 10px;
`;

export const SubCommentInput = styled.input`
    border: none;
    width: 370px;
    height: 40px;
    border-radius: 10px;
    padding-left: 10px;
    font-size: 15px;
`;

export const CancelButton = styled.div`
    margin: 10px 0 0 10px;
    cursor: pointer;

    ${LiftUp}
`;
