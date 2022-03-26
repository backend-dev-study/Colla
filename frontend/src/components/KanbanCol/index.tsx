import React, { FC, useState } from 'react';
import { useDrop } from 'react-dnd';

import { useLocation } from 'react-router-dom';
import deleteIconImg from '../../../public/assets/images/close-circle.svg';
import plusIconImg from '../../../public/assets/images/plus-circle.svg';
import { updateTaskStatus } from '../../apis/task';
import useInputTask from '../../hooks/useInputTask';
import useModal from '../../hooks/useModal';
import { ItemType, TaskType } from '../../types/kanban';
import { StateType } from '../../types/project';
import { TaskModal } from '../Modal/Task';
import DeleteTaskStatusModal from '../Modal/TaskStatus/Delete';
import Task from '../Task';
import { Wrapper, KanbanStatus, KanbanIssue, AddTaskButton, PlusIcon, DeleteStatusButton, DeleteIcon } from './style';

interface PropType {
    statuses: Array<string>;
    status: string;
    taskList: TaskType[];
    tasks: TaskType[];
    changeColumn: Function;
    moveTaskHandler: Function;
}

const KanbanCol: FC<PropType> = ({ statuses, status, taskList, tasks, changeColumn, moveTaskHandler }) => {
    const { state } = useLocation<StateType>();
    const { clearInputTask } = useInputTask();
    const [taskId, setTaskId] = useState<number | null>(null);
    const { Modal, setModal } = useModal();
    const { Modal: StatusModal, setModal: setStatusModal } = useModal();

    const handleUpdateTask = async (taskId: number, statusName: string) => {
        await updateTaskStatus(state.projectId, taskId, statusName);
    };
    const [, drop] = useDrop({
        accept: 'task_type',
        drop: (item: ItemType) => {
            handleUpdateTask(item.id, status);
            return { name: status };
        },
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
                    <DeleteStatusButton onClick={setStatusModal}>
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
                <TaskModal taskId={taskId} status={status} taskList={taskList} hideModal={setModal} page="kanban" />
            </Modal>
            <StatusModal>
                <DeleteTaskStatusModal statuses={statuses} status={status} />
            </StatusModal>
        </>
    );
};

export default KanbanCol;
