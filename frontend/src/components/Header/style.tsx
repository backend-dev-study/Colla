import styled from '@emotion/styled';
import { Center, WidthCenter } from '../../styles/common';

export const Container = styled.div`
    display: flex;
    width: 80vw;
    height: 100px;
    margin-left: 15vw;
    justify-content: space-between;
`;

export const ProjectTitle = styled.div`
    overflow: hidden;
    min-width: 120px;
    white-space: nowrap;
    font-size: 36px;
    text-overflow: ellipsis;
    margin-left: 20px;
`;

export const ProjectDesc = styled.div`
    position: absolute;
    top: 40px;
    font-size: 24px;
    visibility: hidden;
    width: 400px;
    margin-left: 30px;

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
    display: flex;
    justify-content: start;
    align-items: center;
    flex: 10;
    height: 100%;
`;

export const RightNav = styled.div`
    height: 100%;
    margin-right: 50px;

    ${Center}
`;
