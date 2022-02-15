import styled from '@emotion/styled';

import { LIGHT_GRAY, RED, WHITE } from '../../../../styles/color';
import { Column, Modal } from '../../../../styles/common';

export const Wrapper = styled.div`
    position: absolute;
    top: 0px;
    left: 30vw;
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

export const Status = styled.div`
    width: 300px;
    align-items: center;

    ${Column}
`;
export const CurrentStatus = styled.div`
    width: 180px;
    text-align: center;
    border-radius: 5px;
    background: ${WHITE};
`;

export const SelectedStatus = styled.div`
    position: relative;
    text-align: center;
    width: 180px;
    border-radius: 5px;
    background: ${WHITE};
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
