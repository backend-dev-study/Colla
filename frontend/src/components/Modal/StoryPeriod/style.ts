import styled from '@emotion/styled';
import { ModalContainer } from '../../../styles/modal';

export const Container = styled.div`
    width: 400px;
    height: 180px;
    margin: 130px 0 0 235px;
    top: 200px;
    left: 500px;

    ${ModalContainer}
`;

export const Period = styled.div`
    display: flex;
    flex-direction: column;
    margin: 30px 0 0 30px;

    span {
        margin-right: 15px;
    }

    span:first-child {
        font-size: 18px;
    }

    div {
        margin: 30px 0 15px 0;
    }
`;

export const DatePicker = styled.input`
    width: 140px;
    background-color: transparent;
    outline: none;
`;
