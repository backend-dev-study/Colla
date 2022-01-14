import React from 'react';

import HomeImageSrc from '../../../public/assets/images/home-image.png';
import Header from '../../components/Header';
import ProjectModal from '../../components/Modal/Project';
import { SideBar } from '../../components/SideBar';
import useModal from '../../hooks/useModal';
import { Container, HomeImage, ProjectNotice } from './style';

const projects = [
    {
        name: '프로젝트 1',
    },
    {
        name: '프로젝트 2',
    },
];

const Home = () => {
    const { setModal, Modal } = useModal();

    return (
        <>
            <Header />
            <SideBar props={projects} project />
            <Container>
                <HomeImage src={HomeImageSrc} />
                <ProjectNotice onClick={setModal}>프로젝트를 추가해보세요!</ProjectNotice>
                <Modal>
                    <ProjectModal />
                </Modal>
            </Container>
        </>
    );
};
export default Home;
