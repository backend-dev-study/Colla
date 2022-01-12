import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';

import HomeImageSrc from '../../../public/assets/images/home-image.png';
import { isResponseSuccess } from '../../apis/common';
import { createProject } from '../../apis/user';
import Header from '../../components/Header';
import ProjectModal from '../../components/Modal/Project';
import { projectDescState, projectNameState } from '../../stores/projectState';
import { Container, HomeImage, ProjectNotice } from './style';

const Home = () => {
    const [projectModal, setProjectModal] = useState(false);
    const setProjectName = useSetRecoilState(projectNameState);
    const setProjectDesc = useSetRecoilState(projectDescState);
    const history = useHistory();

    const handleModal = () => setProjectModal(!projectModal);

    const sendRequest = async (userId: number, name: string, desc: string) => {
        const response = await createProject(userId, name, desc);

        if (isResponseSuccess(response.status)) {
            const { name: projectName, description: projectDesc } = response.data;
            setProjectName(projectName);
            setProjectDesc(projectDesc);
            history.push('/kanban', response.data);
        } else {
            // eslint-disable-next-line no-alert
            alert('프로젝트 생성에 실패했습니다.');
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
