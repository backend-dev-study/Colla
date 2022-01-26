import React, { FC } from 'react';

import StarImgSrc from '../../../../public/assets/images/star.png';
import { TaskType } from '../../../types/kanban';
import { Star } from '../../Task/style';
import { Priority, Task, TaskList, TaskTitle } from './style';

interface PropType {
    taskList: TaskType[];
    setPreTaskList: Function;
    setPreTaskVisible: Function;
}

export const PreTaskDropDown: FC<PropType> = ({ taskList, setPreTaskList, setPreTaskVisible }) => {
    const addPreTask = (idx: number) => {
        setPreTaskList((prev: TaskType[]) => [...prev, idx]);
        setPreTaskVisible();
    };

    return (
        <TaskList>
            {taskList.map((task, idx) => (
                <Task key={idx} onClick={() => addPreTask(idx)}>
                    <TaskTitle>{task.title}</TaskTitle>
                    <Priority>
                        {Array(task.priority)
                            .fill(0)
                            .map((el, i) => i + 1)
                            .map((el) => (
                                <Star key={el} src={StarImgSrc} />
                            ))}
                    </Priority>
                </Task>
            ))}
        </TaskList>
    );
};