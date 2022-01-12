import styled from '@emotion/styled';
import { Center, WidthCenter } from '../../styles/common';

export const Container = styled.div`
    display: flex;
    width: 90vw;
    height: 100px;
    margin-left: 100px;
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
    flex: 10;
    height: 100%;

    ${Center}
`;

export const RightNav = styled.div`
    height: 100%;
    margin-top: 20px;

    ${Center}
`;
