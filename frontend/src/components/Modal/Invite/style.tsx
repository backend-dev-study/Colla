import styled from '@emotion/styled';
import { GRAY, GREEN } from '../../../styles/color';
import { Modal } from '../../../styles/common';

export const Wrapper = styled.div`
    position: absolute;
    justify-content: space-around;
    width: 400px;
    height: 300px;
    font-size: 20px;
    border-radius: 40px;
    background: ${GRAY};
    padding: 40px 20px;

    top: 10px;

    ${Modal};
`;

export const InviteUser = styled.div`
    display: flex;
    justify-content: space-around;
`;

export const InviteEmailInput = styled.input`
    width: 300px;
    height: 35px;
    border-radius: 10px;
    font-size: 20px;
`;
export const InviteButton = styled.button`
    width: 80px;
    height: 35px;
    border-radius: 10px;
    background: ${GREEN};
    font-size: 20px;
`;
