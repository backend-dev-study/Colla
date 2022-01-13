import styled from '@emotion/styled';
import { Center } from '../../styles/common';

export const Container = styled.div`
    flex-direction: column;

    ${Center}
`;

export const HomeImage = styled.img`
    width: 836px;
    height: 680px;
`;

export const ProjectNotice = styled.div`
    font-size: 30px;
    margin-top: -50px;
    cursor: pointer;

    :hover {
        animation: lift-up 0.5s forwards;
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
