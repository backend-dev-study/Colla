import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';

import HomeImageSrc from '../../../public/assets/images/home-image.png';
import { createProject } from '../../apis/user';
import Header from '../../components/Header';
import ProjectModal from '../../components/Modal/Project';
import { Container, HomeImage, ProjectNotice } from './style';

const Home = () => {
    const [projectModal, setProjectModal] = useState(false);
    const history = useHistory();

    const handleModal = () => setProjectModal(!projectModal);

    const sendRequest = async (userId: number, name: string, desc: string) => {
        const response = await createProject(userId, name, desc);
        history.push('/kanban', response.data);
    };

    return (
        <>
            <Header />
            <Container>
                <HomeImage src={HomeImageSrc} />
                <ProjectNotice onClick={handleModal}>프로젝트를 추가해보세요!</ProjectNotice>
                {projectModal ? <ProjectModal onClick={sendRequest} /> : null}
            </Container>
        </>
    );
};
export default Home;
