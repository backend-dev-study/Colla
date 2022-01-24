import React, { FC } from 'react';

import { useDrop } from 'react-dnd';
import { ItemType, TaskType } from '../../types/kanban';
import Task from '../Task';
import { Wrapper, KanbanStatus, KanbanIssue } from './style';

interface PropType {
    status: string;
    tasks: TaskType[];
    changeColumn: Function;
    moveTaskHandler: Function;
}

const KanbanCol: FC<PropType> = ({ status, tasks, changeColumn, moveTaskHandler }) => {
    const [, drop] = useDrop({
        accept: 'task_type',
        drop: () => ({ name: status }),
        collect: (monitor) => ({
            isOver: monitor.isOver(),
            canDrop: monitor.canDrop(),
        }),
        hover(item: ItemType, monitor) {
            if (monitor.isOver()) {
                changeColumn(item.id, status);
            }
        },
    });

    return (
        <>
            <Wrapper>
                <KanbanStatus>{status}</KanbanStatus>
                <KanbanIssue ref={drop}>
                    {tasks.map((task) => (
                        <Task key={task.id} task={task} changeColumn={changeColumn} moveHandler={moveTaskHandler} />
                    ))}
                </KanbanIssue>
            </Wrapper>
        </>
    );
};

export default KanbanCol;
