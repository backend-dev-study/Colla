import { css } from '@emotion/react';

export const Center = css`
    display: flex;
    justify-content: center;
    align-items: center;
`;

export const WidthCenter = css`
    display: flex;
    justify-content: center;
`;

export const Column = css`
    display: flex;
    flex-direction: column;
`;

export const Shadow = css`
    box-shadow: 0px 20px 4px rgba(0, 0, 0, 0.25);
`;

export const Modal = css`
    ${Column}
    ${Shadow}
`;

export const LiftUp = css`
    :hover {
        animation: lift-up 0.2s forwards;
    }

    @keyframes lift-up {
        0% {
            transform: translateY(0);
        }
        100% {
            transform: translateY(-6px);
        }
    }
`;
