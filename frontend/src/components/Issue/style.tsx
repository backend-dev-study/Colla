import styled from '@emotion/styled';

import { GREEN, WHITE } from '../../styles/color';

interface Props {
    story?: boolean;
}

const BASE = 900;
const EXTRA = 100;

export const Wrapper = styled.div<Props>`
    position: relative;
    display: flex;
    align-items: center;
    padding-left: 20px;
    width: ${({ story }) => (story ? `${BASE + EXTRA}px` : `${BASE}px`)};
    height: 40px;
    border-radius: 20px;
    background: ${({ story }) => (story ? GREEN : WHITE)};
    margin-left: ${({ story }) => (story ? '0px' : `${EXTRA}px`)};
    margin-top: 13px;
    margin-bottom: 13px;
    font-size: 20px;
`;

export const Attributes = styled.div`
    position: absolute;
    right: 20px;
    display: flex;
`;
