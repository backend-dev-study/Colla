import React, { FC } from 'react';

import { TaskStatusList, TaskStatus, TaskStatusName } from './style';

interface PropType {
    taskStatuses: Array<string>;
    setSelected: Function;
    setVisible: Function;
}

export const TaskStatusDropdown: FC<PropType> = ({ taskStatuses, setSelected, setVisible }) => {
    const changeTaskStatus = (taskStatus: string) => {
        setSelected(taskStatus);
        setVisible();
    };

    return (
        <TaskStatusList>
            {taskStatuses.map((name, idx) => (
                <TaskStatus key={idx} onClick={() => changeTaskStatus(name)}>
                    <TaskStatusName>{name}</TaskStatusName>
                </TaskStatus>
            ))}
        </TaskStatusList>
    );
};
