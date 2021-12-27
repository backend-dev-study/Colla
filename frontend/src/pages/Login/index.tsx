import React from 'react';
import styled from '@emotion/styled';

import GithubLogoSrc from '../../../public/assets/images/github-logo.png';
import MainImageSrc from '../../../public/assets/images/main-image.jpg';
import { Center } from '../../styles/common';

const Login = () => (
    <Container>
        <Logo>Colla</Logo>
        <MainImage src={MainImageSrc} />
        <LoginButton>
            <LoginLogo src={GithubLogoSrc} />
            <LoginInfo>github으로 로그인</LoginInfo>
        </LoginButton>
    </Container>
);

const Container = styled.div`
    ${Center}
    flex-direction: column;
    margin-top: 70px;
`;

const Logo = styled.div`
    ${Center}
    font-family: 'Euljiro';
    font-size: 50px;
    font-weight: 1000;
`;

const MainImage = styled.img`
    width: 900px;
    height: 600px;
    margin-top: 20px;
`;

const LoginButton = styled.div`
    ${Center}
    width: 500px;
    height: 70px;
    border-radius: 10px;
    background-color: #cee8cf;
    cursor: pointer;
`;

const LoginLogo = styled.img`
    width: 40px;
    height: 40px;
    margin-right: 10px;
`;

const LoginInfo = styled.span`
    font-family: 'Euljiro';
    font-size: 30px;
`;

export default Login;
