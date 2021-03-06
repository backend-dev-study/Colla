import { css } from '@emotion/react';
import styled from '@emotion/styled';

import { LIGHT_GRAY, RED } from '../../../styles/color';
import { LiftUp } from '../../../styles/common';

export const ModalContainer = styled.div`
    display: flex;
    flex-direction: column;
    width: 880px;
    min-height: 430px;
    border-radius: 20px;
    margin-left: 220px;
    background-color: ${LIGHT_GRAY};
    box-shadow: 0 4px 4px rgba(0, 0, 0, 0.1), 0 4px 20px rgba(0, 0, 0, 0.1);
    z-index: 10;
`;

export const Container = styled.div`
    display: flex;
    flex-direction: row;
`;

export const DownIcon = styled.img`
    width: 15px;
    height: 15px;
    margin: 0 12px 0 auto;
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
    color: ${RED};

    ${Button}
`;

export const CompleteButton = styled.div`
    margin-right: 20px;

    ${Button};
`;
