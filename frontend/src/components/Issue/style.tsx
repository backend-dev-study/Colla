import styled from '@emotion/styled';

import { GREEN, WHITE } from '../../styles/color';
import { WidthCenter } from '../../styles/common';

interface Props {
    story?: boolean;
}

export const Wrapper = styled.div<Props>`
    width: 400px;
    height: 40px;
    border-radius: 20px;
    background: ${({ story }) => (story ? GREEN : WHITE)};
    margin-left: ${({ story }) => (story ? '0px' : '100px')};
    margin-top: 13px;
    margin-bottom: 13px;
    align-items: center;
    font-size: 20px;

    ${WidthCenter}
`;
