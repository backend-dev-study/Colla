import React, { useState } from 'react';

import HomeImageSrc from '../../../public/assets/images/home-image.png';
import Header from '../../components/Header';
import { Container, HomeImage, ProjectNotice } from './style';

const Home = () => {
    const [projectModal, setProjectModal] = useState(false);
    const handleModal = () => setProjectModal(!projectModal);

    return (
        <>
            <Header />
            <Container>
                <HomeImage src={HomeImageSrc} />
                <ProjectNotice onClick={handleModal}>프로젝트를 추가해보세요!</ProjectNotice>
            </Container>
        </>
    );
};
export default Home;
