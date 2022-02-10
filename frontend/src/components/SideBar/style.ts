import { css } from '@emotion/react';
import styled from '@emotion/styled';
import { GREEN } from '../../styles/color';

export const VerticalBar = styled.div`
    position: fixed;
    height: 75vh;
    margin: -10px 0 0 30px;
    border: 2px solid ${GREEN};
    background-color: ${GREEN};
    z-index: 3;
`;

export const ProjectContainer = styled.div`
    position: absolute;
    overflow-y: scroll;
    padding-top: 10px;
    width: 200px;
    height: 75vh;
    z-index: 2;

    ::-webkit-scrollbar {
        display: none;
    }
`;

export const Wrapper = css`
    display: flex;
    flex-direction: column;
    justify-content: space-around;
`;

const SideBarComponent = css`
    cursor: pointer;

    :hover {
        animation: lift-up 0.2s forwards;
    }

    @keyframes lift-up {
        0% {
            transform: translateY(0);
        }
        100% {
            transform: translateY(-6px);
        }
    }
`;

export const ProjectWrapper = styled.div`
    ${Wrapper}
`;

export const Project = styled.div`
    display: flex;
    flex-direction: row;
    align-items: center;
    margin: 30px 30px 15px 50px;

    ${SideBarComponent}
`;

export const ProjectIcon = styled.img`
    width: 70px;
    height: 70px;
    border-radius: 10px;
    cursor: pointer;
    object-fit: cover;
    margin-right: 10px;
`;

export const MenuContainer = styled.div`
    position: fixed;
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 200px;
    height: 75vh;
    z-index: 2;
`;

export const MenuWrapper = styled.div`
    ${Wrapper}
`;

export const Menu = styled.div`
    font-size: 20px;
    margin: 30px 30px 15px 50px;

    ${SideBarComponent}
`;
