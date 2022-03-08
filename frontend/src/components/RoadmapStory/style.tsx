import styled from '@emotion/styled';

import { GREEN } from '../../styles/color';

interface PropType {
    width: number;
}

const WEEK = 14;
const PER_DAY = 100 % WEEK;

export const Story = styled.div<PropType>`
    width: ${({ width }) => `${width * PER_DAY}%`};
    height: 50px;
    border-radius: 10px;
    background: ${GREEN};
`;
