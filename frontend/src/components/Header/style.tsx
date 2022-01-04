import styled from '@emotion/styled';
import { Center } from '../../styles/common';

export const Container = styled.div`
    display: flex;
    width: 85vw;
    height: 200px;
`;

export const LeftNav = styled.div`
    flex: 8;
    height: 100%;

    ${Center}
`;

export const RightNav = styled.div`
    flex: 2;
    height: 100%;

    ${Center}
`;
