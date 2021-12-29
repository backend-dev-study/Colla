import React from 'react';

import GithubLogoSrc from '../../../public/assets/images/github-logo.png';
import MainImageSrc from '../../../public/assets/images/main-image.jpg';
import { Container, Logo, MainImage, LoginButton, GithubLogo, LoginInfo } from './style';

const Login = () => {
    const loginWithGithub = () => {
        window.open(process.env.GITHUB_AUTH_URL, '_self');
    };

    return (
        <Container>
            <Logo>Colla</Logo>
            <MainImage src={MainImageSrc} />
            <LoginButton onClick={loginWithGithub}>
                <GithubLogo src={GithubLogoSrc} />
                <LoginInfo>github으로 로그인</LoginInfo>
            </LoginButton>
        </Container>
    );
};

export default Login;
