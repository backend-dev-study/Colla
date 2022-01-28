import { css } from '@emotion/react';
import { GRAY } from './color';

export const List = css`
    position: absolute;
    display: flex;
    flex-direction: column;
    max-height: 300px;
    border: 1px solid ${GRAY};
    border-radius: 10px;
    padding-left: 10px;
    background-color: #fff;
    overflow-y: scroll;

    &::-webkit-scrollbar {
        width: 8px;
    }

    &::-webkit-scrollbar-thumb {
        background: #bbbbbb;
        border-radius: 10px;
    }
`;

export const Work = css`
    display: flex;
    justify-content: space-between;
    padding: 10px;
    margin-right: 10px;
    border-bottom: 1px solid #000;

    :last-child {
        border: none;
    }

    :hover {
        background-color: #f1f1f1;
        cursor: pointer;
    }
`;

export const Title = css`
    max-width: 260px;
`;
