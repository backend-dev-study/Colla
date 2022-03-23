import styled from '@emotion/styled';

const ROUND = 2 * Math.PI;

interface PropType {
    radius: number;
    progress: number;
    strokeWidth: number;
    strokeColor: string;
}

export const ProgressCircle = styled.circle<PropType>`
    position: relative;
    cx: 50%;
    cy: 50%;
    r: ${({ radius }) => radius}px;
    fill: none;
    stroke-width: ${({ strokeWidth }) => strokeWidth};
    stroke-dasharray: ${({ radius }) => ROUND * radius};
    stroke-dashoffset: ${({ progress, radius }) => ROUND * radius * ((100 - progress) / 100)};
    stroke: ${({ strokeColor }) => strokeColor};
`;
