import React, { FC, useRef } from 'react';
import { useDrag, useDrop } from 'react-dnd';

import { ItemType, TaskType } from '../../types/kanban';
import { Wrapper } from './style';

interface PropType {
    task: TaskType;
    changeColumn: Function;
    moveHandler: Function;
}

const Task: FC<PropType> = ({ task, changeColumn, moveHandler }) => {
    const { id, title, managerName, priority, index } = task;
    const [{ isDragging }, drag] = useDrag({
        type: 'task_type',
        item: { id, index, name: 'task', type: 'task_type' },
        collect: (monitor) => ({
            isDragging: monitor.isDragging(),
        }),
        end: (item, monitor) => {
            const dropResult: any = monitor.getDropResult();
            if (dropResult) {
                changeColumn(id, dropResult.name);
            }
        },
    });

    const ref = useRef<HTMLDivElement>(null);

    const [, drop] = useDrop({
        accept: 'task_type',
        hover(item: ItemType, monitor) {
            if (!ref.current) {
                return;
            }

            const dragIndex = item.index;
            const hoverIndex = index!;
            if (dragIndex === hoverIndex) {
                return;
            }

            const hoverBoundingRect = ref.current.getBoundingClientRect();
            const hoverMiddleY = (hoverBoundingRect.bottom - hoverBoundingRect.top) / 2;
            const clientOffset = monitor.getClientOffset();

            const hoverClientY = clientOffset!.y - hoverBoundingRect.top;

            if (dragIndex < hoverIndex && hoverClientY < hoverMiddleY) {
                return;
            }

            if (dragIndex > hoverIndex && hoverClientY > hoverMiddleY) {
                return;
            }
            moveHandler(dragIndex, hoverIndex);
            item.index = hoverIndex;
        },
    });

    drag(drop(ref));

    const opacity = isDragging ? 0.4 : 1;

    return (
        <Wrapper ref={ref} style={{ opacity }}>
            <div>
                {title}
                {priority}
            </div>
            <div>{managerName}</div>
        </Wrapper>
    );
};

export default Task;
