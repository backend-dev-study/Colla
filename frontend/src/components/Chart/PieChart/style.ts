import { keyframes } from '@emotion/react';
import styled from '@emotion/styled';

interface PropType {
    color: string;
}

const ROUND = 2 * Math.PI;

export const Svg = styled.svg`
    width: 100%;
    height: 100%;
`;

export const Colors = styled.div`
    display: flex;
    align-items: center;
`;

export const StatusName = styled.div`
    text-align: center;
`;

export const Color = styled.div<PropType>`
    width: 50px;
    height: 30px;
    background: ${({ color }) => color};
    margin: 0 20px 0 10px;
`;

const draw = keyframes`
  from {
    stroke-dashoffset: 0%;
  }
  to {
    stroke-dashoffset: 100%;
  }
`;

interface CircleType {
    radius: number;
    progress: number;
    strokeWidth: number;
    strokeColor: string;
}

export const AnimatedSvg = styled.svg`
    position: absolute;
    width: 100%;
    height: 100%;
`;

export const AbsoluteCircle = styled.circle<CircleType>`
    cx: 50%;
    cy: 50%;
    r: ${({ radius }) => radius}px;
    fill: none;
    stroke-width: ${({ strokeWidth }) => strokeWidth};
    stroke-dasharray: ${({ radius }) => ROUND * radius};
    stroke-dashoffset: ${({ progress, radius }) => ROUND * radius * ((100 - progress) / 100)};
    stroke: ${({ strokeColor }) => strokeColor};
    animation: ${draw} 2s ease-in-out forwards;
`;

export const Wrapper = styled.div`
    position: relative;
    width: 100%;
    height: 100%;
`;
