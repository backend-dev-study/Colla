import React from 'react';

import HomeImageSrc from '../../../public/assets/images/home-image.png';
import Header from '../../components/Header';
import { Container, HomeImage, ProjectNotice } from './style';

const Home = () => (
    <>
        <Header />
        <Container>
            <HomeImage src={HomeImageSrc} />
            <ProjectNotice>프로젝트를 추가해보세요!</ProjectNotice>
        </Container>
    </>
);

export default Home;
