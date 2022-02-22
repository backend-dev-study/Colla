import styled from '@emotion/styled';
import { Center, WidthCenter } from '../../styles/common';

export const Container = styled.div`
    display: flex;
    width: 100vw - 150px;
    height: 100px;
    margin-left: 150px;
`;

export const ProjectTitle = styled.div`
    font-size: 36px;
`;

export const ProjectDesc = styled.div`
    position: absolute;
    top: 40px;
    font-size: 24px;
    visibility: hidden;
    width: 400px;

    ${WidthCenter}
`;

export const ProjectInfo = styled.div`
    position: relative;
    &:hover ${ProjectDesc} {
        visibility: visible;
    }

    ${WidthCenter}
`;

export const ProjectManagement = styled.div`
    position: relative;
`;

export const ProjectManageButton = styled.button`
    position: relative;
    width: 80px;
    height: 35px;
    border-radius: 10px;
    margin-left: 20px;
    margin-right: 20px;
    font-size: 20px;
`;

export const LeftNav = styled.div`
    flex: 10;
    height: 100%;
    margin-right: 1000px;

    ${Center}
`;

export const RightNav = styled.div`
    height: 100%;
    margin-right: 50px;

    ${Center}
`;
