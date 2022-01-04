import React, { FC } from 'react';

import { Container, ImageContainer } from './style';

interface PropType {
    projectName: string;
    image?: string;
    onClick: () => void;
}

const ProjectIcon: FC<PropType> = ({ projectName, image, onClick }) => (
    <div onClick={onClick}>
        {image ? <ImageContainer image={image} /> : <Container>{projectName[0].toUpperCase()}</Container>}
    </div>
);

export default ProjectIcon;
