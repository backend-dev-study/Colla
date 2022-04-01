import styled from '@emotion/styled';

import { ROADMAP_DATES_LIMIT } from '../../constants';
import { GREEN } from '../../styles/color';
import { HeightCenter } from '../../styles/common';

interface PropType {
    width: number;
    left: number;
}

const PER_DAY = 90 / ROADMAP_DATES_LIMIT;

export const Wrapper = styled.div`
    margin-left: 5px;
    margin-right: 5px;
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
    cursor: pointer;

    ${HeightCenter}
`;
