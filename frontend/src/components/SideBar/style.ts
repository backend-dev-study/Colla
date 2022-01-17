import { css } from '@emotion/react';
import styled from '@emotion/styled';
import { GREEN } from '../../styles/color';

export const VerticalBar = styled.div`
    position: absolute;
    height: 85vh;
    margin: -10px 0 0 30px;
    border: 2px solid ${GREEN};
    background-color: ${GREEN};
`;

export const ProjectContainer = styled.div`
    position: absolute;
    height: 770px;
    overflow-y: scroll;
    padding-top: 10px;

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
    margin: 0 0 15px 50px;

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
    position: absolute;
    height: 770px;
    display: flex;
    flex-direction: column;
    justify-content: center;
`;

export const MenuWrapper = styled.div`
    height: 400px;

    ${Wrapper}
`;

export const Menu = styled.div`
    font-size: 20px;
    margin: 0 0 15px 50px;

    ${SideBarComponent}
`;
