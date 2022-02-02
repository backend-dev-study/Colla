import React, { FC } from 'react';

import DeleteIconSrc from '../../../../public/assets/images/delete.png';
import StarImgSrc from '../../../../public/assets/images/star.png';
import { TaskType } from '../../../types/kanban';
import { Task, TaskTitle } from '../../DropDown/PreTask/style';
import { Star } from '../../Task/style';
import { Container, DeleteButton, PreTask } from './style';

interface PropType {
    preTaskList: Array<number>;
    taskList: Array<TaskType>;
    handleDeletePreTask: Function;
}

export const PreTaskList: FC<PropType> = ({ preTaskList, taskList, handleDeletePreTask }) => (
    <Container>
        {preTaskList.map((taskIdx, idx) => (
            <PreTask key={idx}>
                <Task>
                    <TaskTitle>{taskList[taskIdx].title}</TaskTitle>
                    <div>
                        {Array(taskList[taskIdx].priority)
                            .fill(0)
                            .map((el, i) => i + 1)
                            .map((el) => (
                                <Star key={el} src={StarImgSrc} />
                            ))}
                    </div>
                </Task>
                <DeleteButton src={DeleteIconSrc} onClick={() => handleDeletePreTask(taskIdx)} />
            </PreTask>
        ))}
    </Container>
);
