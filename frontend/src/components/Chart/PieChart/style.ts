import styled from '@emotion/styled';
import { WHITE } from '../../../styles/color';

const RADIUS = 40;
const PROGRESS = 20;
const ROUND = 2 * Math.PI * RADIUS;
const WIDTH = 20;

export const BaseCircle = styled.circle`
    cx: 50%;
    cy: 50%;
    r: ${RADIUS}px;
    fill: none;
    stroke: ${WHITE};
    stroke-width: ${WIDTH};
`;

export const PortionCircle = styled.circle`
    cx: 50%;
    cy: 50%;
    r: ${RADIUS}px;
    fill: none;
    stroke: #c09853;
    stroke-width: ${WIDTH};
    stroke-dasharray: ${ROUND};
    stroke-dashoffset: ${ROUND * ((100 - PROGRESS) / 100)};
`;

export const ThirdCircle = styled.circle`
    cx: 50%;
    cy: 50%;
    r: ${RADIUS}px;
    fill: none;
    stroke: #008000;
    stroke-width: ${WIDTH};
    stroke-dasharray: ${ROUND};
    stroke-dashoffset: ${ROUND * ((100 - 50) / 100)};
`;
