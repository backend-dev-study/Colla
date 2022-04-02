import React, { FC, useEffect, useState } from 'react';

import { useLocation } from 'react-router-dom';
import { getStoryTasks } from '../../../apis/task';
import { StateType } from '../../../types/project';
import { TaskType } from '../../../types/roadmap';
import { Issue, IssueContents, List } from '../Story/style';
import { Tag, Title } from './style';

interface PropType {
    handleStoryVisible: Function;
    story: number;
}

const TaskList: FC<PropType> = ({ handleStoryVisible, story }) => {
    const { state } = useLocation<StateType>();
    const [taskList, setTaskList] = useState<Array<TaskType>>([]);

    useEffect(() => {
        (async () => {
            const res = await getStoryTasks(state.projectId, story);
            setTaskList(res.data);
        })();
    }, []);

    return (
        <>
            <Title onClick={() => handleStoryVisible()}>스토리 목록 보기</Title>
            <List>
                {taskList.map(({ title, manager, tags }, idx) => (
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

export default TaskList;
