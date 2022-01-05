import styled from '@emotion/styled';
import { Center, WidthCenter } from '../../styles/common';

export const Container = styled.div`
    display: flex;
    width: 85vw;
    height: 100px;
`;

export const ProjectTitle = styled.div`
    font-size: 36px;
`;

export const ProjectDesc = styled.div`
    position: absolute;
    top: 40px;
    font-size: 24px;
    text-shadow: #303030;
    visibility: hidden;
`;

export const ProjectInfo = styled.div`
    position: relative;
    &:hover ${ProjectDesc} {
        visibility: visible;
    }

    ${WidthCenter}
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
