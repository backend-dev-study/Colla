import React, { FC, useState } from 'react';
import { useDrop } from 'react-dnd';

import deleteIconImg from '../../../public/assets/images/close-circle.svg';
import plusIconImg from '../../../public/assets/images/plus-circle.svg';
import useInputTask from '../../hooks/useInputTask';
import useModal from '../../hooks/useModal';
import { ItemType, TaskType } from '../../types/kanban';
import { TaskModal } from '../Modal/Task';
import Task from '../Task';
import { Wrapper, KanbanStatus, KanbanIssue, AddTaskButton, PlusIcon, DeleteStatusButton, DeleteIcon } from './style';

interface PropType {
    status: string;
    taskList: TaskType[];
    tasks: TaskType[];
    changeColumn: Function;
    moveTaskHandler: Function;
}

const KanbanCol: FC<PropType> = ({ status, taskList, tasks, changeColumn, moveTaskHandler }) => {
    const { clearInputTask } = useInputTask();
    const [taskId, setTaskId] = useState<number | null>(null);
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

    const showCreateTaskModal = (event: React.MouseEvent) => {
        clearInputTask();
        setTaskId(null);
        setModal(event);
    };

    const showTask = (event: React.MouseEvent, selectedId: number) => {
        setTaskId(selectedId);
        setModal(event);
    };

    return (
        <>
            <Wrapper>
                <KanbanStatus>
                    {status}
                    <DeleteStatusButton>
                        <DeleteIcon src={deleteIconImg} />
                    </DeleteStatusButton>
                </KanbanStatus>
                <KanbanIssue ref={drop}>
                    {tasks.map((task) => (
                        <Task
                            key={task.id}
                            task={task}
                            changeColumn={changeColumn}
                            moveHandler={moveTaskHandler}
                            showTask={showTask}
                        />
                    ))}
                    <AddTaskButton onClick={(event) => showCreateTaskModal(event)}>
                        <PlusIcon src={plusIconImg} />
                        새로 만들기
                    </AddTaskButton>
                </KanbanIssue>
            </Wrapper>
            <Modal>
                <TaskModal taskId={taskId} status={status} taskList={taskList} hideModal={setModal} />
            </Modal>
        </>
    );
};

export default KanbanCol;
