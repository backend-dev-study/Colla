import React, { ChangeEvent, FC, useState } from 'react';

import ProjectIcon from '../../Icon/Project';
import { Container, SubmitButton, ProjectNameInput, ProjectDescInput } from './style';

interface PropType {
    // eslint-disable-next-line no-unused-vars
    onClick: (name: string, desc: string) => void;
}
const ProjectModal: FC<PropType> = ({ onClick }) => {
    const [name, setName] = useState('');
    const [desc, setDesc] = useState('');

    const handleNameChange = (event: ChangeEvent<HTMLInputElement>) => setName(event.target.value);

    const handleDescChange = (event: ChangeEvent<HTMLInputElement>) => setDesc(event.target.value);

    const handleSubmit = () => onClick(name, desc);

    return (
        <Container>
            <ProjectIcon projectName={name} />
            <ProjectNameInput placeholder={'프로젝트 이름'} value={name} onChange={handleNameChange} />
            <ProjectDescInput placeholder={'프로젝트 설명'} value={desc} onChange={handleDescChange} />
            <SubmitButton onClick={handleSubmit}>프로젝트 생성</SubmitButton>
        </Container>
    );
};

export default ProjectModal;
