import React, { ChangeEvent, useState } from 'react';

import { useHistory } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import { createProject } from '../../../apis/user';
import { projectState } from '../../../stores/projectState';
import { createFormData } from '../../../utils/common';
import ProjectIcon from '../../Icon/Project';
import { Container, SubmitButton, ProjectNameInput, ProjectDescInput } from './style';

const ProjectModal = () => {
    const history = useHistory();
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [thumbnail, setThumbnail] = useState<File | null>(null);

    const setProjectState = useSetRecoilState(projectState);

    const handleNameChange = (event: ChangeEvent<HTMLInputElement>) => setName(event.target.value);

    const handleDescChange = (event: ChangeEvent<HTMLInputElement>) => setDescription(event.target.value);

    const handleSubmit = async () => {
        const formData = thumbnail
            ? createFormData({ name, description, thumbnail })
            : createFormData({ name, description });
        const response = await createProject(formData);

        const {
            id: projectId,
            name: projectName,
            description: projectDescription,
            thumbnail: projectThumbnail,
            members,
        } = response.data;
        setProjectState({
            id: projectId,
            name: projectName,
            description: projectDescription,
            thumbnail: projectThumbnail,
            members,
        });

        history.push('/kanban', { projectId });
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
