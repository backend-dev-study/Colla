import styled from '@emotion/styled';

import { GREEN } from '../../styles/color';
import { HeightCenter } from '../../styles/common';

interface PropType {
    width: number;
    left: number;
}

const WEEK = 14;
const PER_DAY = 90 / (WEEK + 1);

export const Wrapper = styled.div`
    ${HeightCenter}
`;

export const StoryTitle = styled.div`
    width: 10%;
    height: 50px;

    ${HeightCenter}
`;

export const Story = styled.div<PropType>`
    width: ${({ width }) => `${width * PER_DAY}%`};
    height: 50px;
    border-radius: 10px;
    margin: 8px 0px 8px ${({ left }) => `${left * PER_DAY}%`};
    background: ${GREEN};

    ${HeightCenter}
`;
