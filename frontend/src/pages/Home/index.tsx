import React, { useEffect, useState } from 'react';

import HomeImageSrc from '../../../public/assets/images/home-image.png';
import { getUserProjects } from '../../apis/user';
import Header from '../../components/Header';
import ProjectModal from '../../components/Modal/Project';
import { SideBar } from '../../components/SideBar';
import useModal from '../../hooks/useModal';
import { Container, HomeImage, ProjectNotice } from './style';

const Home = () => {
    const [projects, setProjects] = useState([]);
    const { setModal, Modal } = useModal();

    useEffect(() => {
        (async () => {
            try {
                const res = await getUserProjects();
                setProjects(res.data);
            } catch (err) {
                setProjects([]);
            }
        })();
    }, []);

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
