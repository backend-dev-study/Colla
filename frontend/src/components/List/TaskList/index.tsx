import React, { FC } from 'react';

import { Title } from './style';

interface PropType {
    showStory: Function;
}

export const TaskList: FC<PropType> = ({ showStory }) => <Title onClick={() => showStory()}> 스토리 목록 보기</Title>;
