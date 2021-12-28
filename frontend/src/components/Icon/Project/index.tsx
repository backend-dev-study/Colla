import React, { FC } from 'react';

import { Container, ImageContainer } from './style';

interface PropType {
    projectName: string;
    image: string;
}

const ProjectIcon: FC<PropType> = ({ projectName, image }) =>
    image === '' ? <Container>{projectName[0].toUpperCase()}</Container> : <ImageContainer image={image} />;

export default ProjectIcon;
