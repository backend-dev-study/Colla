import React, { FC, useEffect } from 'react';

import { Title } from './style';

interface PropType {
    handleStoryVisible: Function;
    story: number;
}

export const TaskList: FC<PropType> = ({ handleStoryVisible, story }) => {
    useEffect(() => {
        (async () => {
            // TODO: story에 해당하는 태스크 목록 조회하기
        })();
    }, []);

    return <Title onClick={() => handleStoryVisible()}>스토리 목록 보기</Title>;
};
