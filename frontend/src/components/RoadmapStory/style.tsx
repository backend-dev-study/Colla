import styled from '@emotion/styled';

import { GREEN } from '../../styles/color';
import { HeightCenter } from '../../styles/common';

export const Wrapper = styled.div`
    margin-left: 5px;
    margin-right: 5px;
    ${HeightCenter}
`;

interface RowType {
    row: number;
}

export const StoryTitle = styled.div<RowType>`
    grid-row-start: ${({ row }) => row};
    height: 50px;

    ${HeightCenter}
`;

interface PropType {
    row: number;
    width: number;
    left: number;
}

export const Story = styled.div<PropType>`
    grid-row-start: ${({ row }) => row};
    grid-column-start: ${({ left }) => left + 2};
    grid-column-end: ${({ width }) => `span ${width}`};
    height: 50px;
    border-radius: 10px;
    margin: 8px 0 8px 0;
    background: ${GREEN};
    cursor: pointer;

    ${HeightCenter}
`;
