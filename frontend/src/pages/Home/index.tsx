import React from 'react';
import { useHistory } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';

import HomeImageSrc from '../../../public/assets/images/home-image.png';
import { createProject } from '../../apis/user';
import Header from '../../components/Header';
import ProjectModal from '../../components/Modal/Project';
import useModal from '../../hooks/useModal';
import { projectDescState, projectNameState } from '../../stores/atom';
import { Container, HomeImage, ProjectNotice } from './style';

const Home = () => {
    const setProjectName = useSetRecoilState(projectNameState);
    const setProjectDesc = useSetRecoilState(projectDescState);
    const history = useHistory();

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

    const { setModal, Modal } = useModal();

    return (
        <>
            <Header />
            <Container>
                <HomeImage src={HomeImageSrc} />
                <ProjectNotice onClick={setModal}>프로젝트를 추가해보세요!</ProjectNotice>
                <Modal>
                    <ProjectModal onClick={sendRequest} />
                </Modal>
            </Container>
        </>
    );
};
export default Home;
