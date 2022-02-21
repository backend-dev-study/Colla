import React, { FC } from 'react';
import { Wrapper } from './style';

interface PropType {
    story?: boolean;
    title: string;
}

const Issue: FC<PropType> = ({ story, title }) => <Wrapper story={story}>{title}</Wrapper>;

export default Issue;
