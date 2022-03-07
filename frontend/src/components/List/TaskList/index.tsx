import React, { FC, useEffect } from 'react';

import { Issue, IssueContents, List } from '../StoryList/style';
import { Tag, Title } from './style';

interface PropType {
    handleStoryVisible: Function;
    story: number;
}

const dummy = [
    {
        title: 'task1',
        manager: 'yeongkee',
        tags: ['backend', 'refactoring', 'bug fix'],
        priority: 5,
    },
    {
        title: 'task2',
        manager: null,
        tags: ['backend', 'refactoring', 'bug fix', 'frontend', 'document'],
        priority: 3,
    },
    {
        title: 'task3',
        manager: 'yeongkee',
        tags: [],
        priority: 2,
    },
];

export const TaskList: FC<PropType> = ({ handleStoryVisible, story }) => {
    useEffect(() => {
        (async () => {
            // TODO: story에 해당하는 태스크 목록 조회하기
            console.log(story);
        })();
    }, []);

    return (
        <>
            <Title onClick={() => handleStoryVisible()}>스토리 목록 보기</Title>
            <List>
                {dummy.map(({ title, manager, tags }, idx) => (
                    <Issue key={idx}>
                        <IssueContents>{title}</IssueContents>
                        <IssueContents>{manager ? manager : '담당자 없음'}</IssueContents>
                        <IssueContents>
                            {tags.map((tag, idx) => (
                                <Tag key={idx}>{tag}</Tag>
                            ))}
                        </IssueContents>
                    </Issue>
                ))}
            </List>
        </>
    );
};
