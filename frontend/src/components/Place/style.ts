import styled from '@emotion/styled';

import { LIGHT_GRAY } from '../../styles/color';

export const Wrapper = styled.button`
    width: 400px;
    height: 120px;
    border-radius: 20px;
    margin: 5px 10px 5px 10px;
    padding: 10px;
    background: ${LIGHT_GRAY};

    &:hover {
        opacity: 0.3;
    }
`;
