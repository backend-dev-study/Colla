import styled from '@emotion/styled';

import { GREEN, LIGHT_GRAY, WHITE } from '../../styles/color';
import { LiftUp } from '../../styles/common';

interface Props {
    story?: boolean;
}

const BASE = 900;
const EXTRA = 150;

export const Wrapper = styled.div<Props>`
    position: relative;
    display: flex;
    align-items: center;
    padding: 15px 0 15px 20px;
    width: ${({ story }) => (story ? `${BASE + EXTRA}px` : `${BASE}px`)};
    border-radius: 20px;
    background: ${({ story }) => (story ? GREEN : WHITE)};
    margin: 13px 40px 13px ${({ story }) => (story ? '40px' : `${EXTRA}px`)};
    font-size: 20px;
    cursor: pointer;

    ${LiftUp}
`;

export const Attributes = styled.div`
    position: absolute;
    right: 20px;
    display: flex;
    align-items: center;
`;

export const Priority = styled.div`
    display: flex;
    margin-left: 10px;
    margin-right: 10px;
`;

export const Star = styled.img`
    width: 10px;
    height: 10px;
`;

export const Manager = styled.img`
    width: 40px;
    height: 40px;
    border-radius: 20px;
    background: ${LIGHT_GRAY};
    margin-left: 10px;
    margin-right: 10px;
`;

export const Tags = styled.div`
    display: flex;
    font-size: 15px;
`;

export const Tag = styled.div`
    display: flex;
    justify-content: center;
    padding-left: 5px;
    padding-right: 5px;
    border-radius: 5px;
    margin-left: 5px;
    margin-right: 5px;
    background: ${GREEN};
`;
