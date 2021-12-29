import React, { FC } from 'react';

import { Container, ImageContainer } from './style';

interface PropType {
    projectName: string;
    image: string;
    onClick: () => void;
}

const ProjectIcon: FC<PropType> = ({ projectName, image, onClick }) => (
    <div onClick={onClick}>
        {image === '' ? <Container>{projectName[0].toUpperCase()}</Container> : <ImageContainer image={image} />}
    </div>
);

export default ProjectIcon;
