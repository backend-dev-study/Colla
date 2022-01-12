import styled from '@emotion/styled';
import { GREEN } from '../../styles/color';
import { Center } from '../../styles/common';

export const Container = styled.div`
    flex-direction: column;
    margin-top: 70px;

    ${Center}
`;

export const Logo = styled.div`
    font-family: 'Euljiro';
    font-size: 50px;
    font-weight: 1000;

    ${Center}
`;

export const MainImage = styled.img`
    width: 900px;
    height: 600px;
    margin-top: 20px;
`;

export const LoginButton = styled.button`
    width: 500px;
    height: 70px;
    border-radius: 10px;
    background-color: ${GREEN};

    ${Center}
`;

export const GithubLogo = styled.img`
    width: 40px;
    height: 40px;
    margin-right: 10px;
`;

export const LoginInfo = styled.span`
    font-family: 'Euljiro';
    font-size: 30px;
`;
