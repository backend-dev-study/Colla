import styled from '@emotion/styled';
import { LiftUp } from '../../../../styles/common';

export const Container = styled.div`
    display: flex;
    flex-direction: column;
    width: 530px;
    min-height: 50px;
    margin-left: 40px;
`;

export const Title = styled.div`
    margin: 0 0 10px 5px;
`;

export const Comment = styled.div`
    display: flex;
    flex-direction: row;
`;

export const CommentInput = styled.input`
    border: none;
    width: 450px;
    height: 40px;
    border-radius: 10px;
    padding-left: 10px;
    font-size: 15px;
`;

export const SaveButton = styled.div`
    margin: 10px 0 0 15px;
    cursor: pointer;

    ${LiftUp}
`;
