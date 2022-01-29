import styled from '@emotion/styled';
import { ModalContainer } from '../../../styles/modal';

export const Container = styled.div`
    width: 400px;
    height: 150px;

    ${ModalContainer}
`;

export const Tag = styled.div`
    display: flex;
    flex-direction: column;

    span {
        margin: 20px 0 0 20px;
    }
`;

export const TagInput = styled.input`
    border: none;
    width: 350px;
    height: 35px;
    border-radius: 10px;
    padding-left: 10px;
    font-size: 15px;
    margin: 10px 0 0 15px;
`;
