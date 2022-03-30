import { css } from '@emotion/react';
import styled from '@emotion/styled';

export const Container = styled.div`
    display: flex;
    width: 100%;
    height: 100%;
    justify-content: center;
    align-items: center;
`;

export const Graph = styled.svg`
    width: 31vw;
    height: 34vh;
`;

export const GraphBorderLine = styled.line`
    stroke-width: 1;
    stroke: #000;
`;

export const GraphLineStyle = css`
    stroke-width: 2;
    stroke-dasharray: 400;
    stroke-dashoffset: 400;
    animation: draw 0.5s linear forwards;
    animation-delay: calc(0.3s * var(--idx));

    @keyframes draw {
        0% {
            stroke-dashoffset: 400;
        }
        100% {
            stroke-dashoffset: 0;
        }
    }
`;

export const GraphLine = styled.line`
    ${GraphLineStyle}
`;
