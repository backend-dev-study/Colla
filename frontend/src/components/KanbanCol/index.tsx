import React, { FC } from 'react';
import { useDrop } from 'react-dnd';

import PlusIconSrc from '../../../public/assets/images/plus-circle.svg';
import useModal from '../../hooks/useModal';
import { ItemType, TaskType } from '../../types/kanban';
import { TaskModal } from '../Modal/Task';
import Task from '../Task';
import { Wrapper, KanbanStatus, KanbanIssue, AddTaskButton, PlusIcon } from './style';

interface PropType {
    status: string;
    taskList: TaskType[];
    tasks: TaskType[];
    changeColumn: Function;
    moveTaskHandler: Function;
}

const KanbanCol: FC<PropType> = ({ status, taskList, tasks, changeColumn, moveTaskHandler }) => {
    const { Modal, setModal } = useModal();
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
                    <AddTaskButton onClick={setModal}>
                        <PlusIcon src={PlusIconSrc} />
                        새로 만들기
                    </AddTaskButton>
                </KanbanIssue>
            </Wrapper>
            <Modal>
                <TaskModal status={status} taskList={taskList} hideModal={setModal} />
            </Modal>
        </>
    );
};

export default KanbanCol;
