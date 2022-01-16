import React, { ChangeEvent, useState } from 'react';

import { useHistory } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import { createProject } from '../../../apis/user';
import { projectDescState, projectNameState, projectThumbnailState } from '../../../stores/projectState';
import { createFormData } from '../../../utils/common';
import ProjectIcon from '../../Icon/Project';
import { Container, SubmitButton, ProjectNameInput, ProjectDescInput } from './style';

const ProjectModal = () => {
    const history = useHistory();
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [thumbnail, setThumbnail] = useState<File | null>(null);

    const setProjectName = useSetRecoilState(projectNameState);
    const setProjectDesc = useSetRecoilState(projectDescState);
    const setProjectThumbnail = useSetRecoilState(projectThumbnailState);

    const handleNameChange = (event: ChangeEvent<HTMLInputElement>) => setName(event.target.value);

    const handleDescChange = (event: ChangeEvent<HTMLInputElement>) => setDescription(event.target.value);

    const handleSubmit = async () => {
        try {
            const formData = createFormData({ name, description, thumbnail });
            const response = await createProject(formData);
            const { name: projectName, description: projectDesc, thumbnail: projectThumbnail } = response.data;
            setProjectName(projectName);
            setProjectDesc(projectDesc);
            setProjectThumbnail(projectThumbnail);

            history.push('/kanban', response.data);
        } catch (e: any) {
            // eslint-disable-next-line no-alert
            window.alert(e.response.data.message);
        }
    };

    return (
        <Container>
            <ProjectIcon thumbnail={thumbnail} setThumbnail={setThumbnail} />
            <ProjectNameInput placeholder={'프로젝트 이름'} value={name} onChange={handleNameChange} />
            <ProjectDescInput placeholder={'프로젝트 설명'} value={description} onChange={handleDescChange} />
            <SubmitButton onClick={handleSubmit}>프로젝트 생성</SubmitButton>
        </Container>
    );
};

export default ProjectModal;
