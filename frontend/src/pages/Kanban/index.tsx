import React, { useEffect, useState } from 'react';
import { useHistory, useLocation } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';

import PlusIcon from '../../../public/assets/images/plus-circle.svg';
import { getProject } from '../../apis/project';
import Header from '../../components/Header';
import KanbanCol from '../../components/KanbanCol';
import TaskStatusModal from '../../components/Modal/TaskStatus';
import { SideBar } from '../../components/SideBar';
import useModal from '../../hooks/useModal';
import { projectState } from '../../stores/projectState';
import { TaskType } from '../../types/kanban';
import { Wrapper, Container, KanbanStatusAddButton, KanbanAddImage } from './style';

interface stateType {
    projectId: number;
}
const menu = ['로드맵', '백로그', '대시보드', '지도'];

const Kanban = () => {
    const history = useHistory();
    const { state } = useLocation<stateType>();
    const [taskList, setTaskList] = useState<Array<TaskType>>([]);
    const [taskStatuses, setTaskStatuses] = useState<Array<string>>([]);
    const setProjectState = useSetRecoilState(projectState);
    const { Modal, setModal } = useModal();

    if (!state.projectId) {
        history.push('/home');
    }

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
        (async () => {
            const res = await getProject(state.projectId);
            const { id, name, description, thumbnail, tasks, members } = res.data;
            setProjectState({
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
    }, []);

    return (
        <>
            <Header />
            <SideBar props={menu} />
            <Container>
                <Wrapper>
                    {taskStatuses.map((value) => (
                        <KanbanCol
                            key={value}
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
                        <TaskStatusModal />
                    </Modal>
                </Wrapper>
            </Container>
        </>
    );
};

export default Kanban;
