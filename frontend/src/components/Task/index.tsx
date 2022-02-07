import React, { FC, useRef } from 'react';
import { useDrag, useDrop } from 'react-dnd';

import StarImgSrc from '../../../public/assets/images/star.png';
import { ItemType, TaskType } from '../../types/kanban';
import { Wrapper, Title, TaskTitle, Priority, Star, Manager, Avatar, Name } from './style';

interface PropType {
    task: TaskType;
    changeColumn: Function;
    moveHandler: Function;
    showTask: Function;
}

const Task: FC<PropType> = ({ task, changeColumn, moveHandler, showTask }) => {
    const { id, title, managerName, avatar, priority, index } = task;
    const ref = useRef<HTMLDivElement>(null);

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
        <Wrapper ref={ref} style={{ opacity }} onClick={(event) => showTask(event, id)}>
            <Title>
                <TaskTitle>{title}</TaskTitle>
                <Priority>
                    {Array(priority)
                        .fill(0)
                        .map((el, i) => i + 1)
                        .map((el) => (
                            <Star key={el} src={StarImgSrc} />
                        ))}
                </Priority>
            </Title>
            <Manager>
                {avatar ? (
                    <>
                        <Avatar src={avatar} />
                        <Name>{managerName}</Name>
                    </>
                ) : (
                    managerName
                )}
            </Manager>
        </Wrapper>
    );
};

export default Task;
