import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';

import HomeImageSrc from '../../../public/assets/images/home-image.png';
import { createProject } from '../../apis/user';
import Header from '../../components/Header';
import ProjectModal from '../../components/Modal/Project';
import { projectDescState, projectNameState } from '../../stores/atom';
import { Container, HomeImage, ProjectNotice } from './style';

const Home = () => {
    const [projectModal, setProjectModal] = useState(false);
    const setProjectName = useSetRecoilState(projectNameState);
    const setProjectDesc = useSetRecoilState(projectDescState);
    const history = useHistory();

    const handleModal = () => setProjectModal(!projectModal);

    const sendRequest = async (userId: number, name: string, desc: string) => {
        try {
            const response = await createProject(userId, name, desc);
            const { name: projectName, description: projectDesc } = response.data;
            setProjectName(projectName);
            setProjectDesc(projectDesc);

            history.push('/kanban', response.data);
        } catch (e: any) {
            // eslint-disable-next-line no-alert
            window.alert(e.response.data.message);
        }
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
