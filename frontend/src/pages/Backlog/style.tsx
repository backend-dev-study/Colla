import styled from '@emotion/styled';
import { LIGHT_GRAY } from '../../styles/color';
import { Column } from '../../styles/common';

export const Container = styled.div`
    align-items: center;

    ${Column}
`;

export const Wrapper = styled.div`
    width: 1300px;
    height: 500px;
    border-radius: 20px;
    overflow-y: scroll;
    background: ${LIGHT_GRAY};
    align-items: center;

    &::-webkit-scrollbar {
        display: none;
    }

    ${Column}
`;
