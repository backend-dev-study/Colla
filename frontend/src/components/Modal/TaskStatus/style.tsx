import styled from '@emotion/styled';

import { LIGHT_GRAY, RED } from '../../../styles/color';
import { Modal } from '../../../styles/common';

export const Wrapper = styled.div`
    width: 300px;
    height: 140px;
    background: ${LIGHT_GRAY};
    border-radius: 20px;
    padding: 30px;
    font-size: 20px;

    justify-content: space-around;
    align-items: center;
    ${Modal}
`;

export const Input = styled.input`
    width: 270px;
    height: 40px;
    border-radius: 10px;
`;

export const ButtonContainer = styled.div`
    width: 100%;
    display: flex;
    justify-content: space-between;
`;

export const Submit = styled.button``;

export const Cancel = styled.button`
    color: ${RED};
`;
