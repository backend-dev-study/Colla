import React, { useEffect, useState } from 'react';
import { useRecoilState } from 'recoil';

import PlusIcon from '../../../public/assets/images/plus-circle.svg';
import { getProject } from '../../apis/project';
import Header from '../../components/Header';
import KanbanCol from '../../components/KanbanCol';
import CreateTaskStatusModal from '../../components/Modal/TaskStatus/Create';
import { SideBar } from '../../components/SideBar';
import useModal from '../../hooks/useModal';
import { projectState } from '../../stores/projectState';
import { TaskType } from '../../types/kanban';
import { menu } from '../common';
import { Wrapper, Container, KanbanStatusAddButton, KanbanAddImage } from './style';

const Kanban = () => {
    const [taskList, setTaskList] = useState<Array<TaskType>>([]);
    const [taskStatuses, setTaskStatuses] = useState<Array<string>>([]);
    const [project, setProject] = useRecoilState(projectState);
    const { Modal, setModal } = useModal();

    const changeTaskColumn = (currentTaskId: number, newColumnName: string) => {
        setTaskList((prevState) =>
            prevState.map((task: TaskType) => ({
                ...task,
                column: task.id === currentTaskId ? newColumnName : task.column,
            })),
        );
    };

    const moveTaskHandler = (dragIndex: number, hoverIndex: number) => {
        const dragTask = taskList[dragIndex];

        if (!dragTask) return;

        setTaskList((prevState) => {
            const prevStateArray = [...prevState];

            const hoverTask = prevStateArray.splice(hoverIndex, 1, dragTask);
            prevStateArray.splice(dragIndex, 1, hoverTask[0]);
            return prevStateArray;
        });
    };

    useEffect(() => {
        if (project.id === -1) return;

        (async () => {
            const res = await getProject(project.id);
            const { id, name, description, thumbnail, tasks, members } = res.data;
            setProject({
                id,
                name,
                description,
                thumbnail,
                members,
            });

            const addColumnToTasks = (taskStatus: string): TaskType[] => [
                ...tasks[taskStatus].map((task) => ({ ...task, column: taskStatus })),
            ];

            let newTaskList: TaskType[] = [];
            Object.keys(tasks).forEach((taskStatus: string) => {
                newTaskList = [...newTaskList, ...addColumnToTasks(taskStatus)];
            });
            setTaskStatuses(Object.keys(tasks));
            setTaskList(newTaskList);
        })();
    }, [project.id]);

    return (
        <>
            <Header />
            <SideBar props={menu} />
            <Container>
                <Wrapper>
                    {taskStatuses.map((value) => (
                        <KanbanCol
                            key={value}
                            statuses={taskStatuses}
                            status={value}
                            taskList={taskList}
                            tasks={taskList
                                .map((task, index) => ({ ...task, index }))
                                .filter((task) => task.column === value)}
                            changeColumn={changeTaskColumn}
                            moveTaskHandler={moveTaskHandler}
                        />
                    ))}
                    <KanbanStatusAddButton onClick={setModal}>
                        <KanbanAddImage src={PlusIcon} />
                    </KanbanStatusAddButton>
                    <Modal>
                        <CreateTaskStatusModal />
                    </Modal>
                </Wrapper>
            </Container>
        </>
    );
};

export default Kanban;
