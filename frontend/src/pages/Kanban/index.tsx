import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import Header from '../../components/Header';
import KanbanCol from '../../components/KanbanCol';
import { SideBar } from '../../components/SideBar';
import { projectState } from '../../stores/projectState';
import { TaskType } from '../../types/kanban';
import { Wrapper, KanbanAddButton, KanbanAdditional, Container } from './style';

const dummyTasks = [
    { id: 1, name: 'task 1', column: 'To Do' },
    { id: 2, name: 'task 2', column: 'To Do' },
    { id: 3, name: 'task 3', column: 'To Do' },
    { id: 4, name: 'task 4', column: 'In Progress' },
    { id: 5, name: 'task 5', column: 'In Progress' },
    { id: 6, name: 'task 6', column: 'In Progress' },
    { id: 7, name: 'task 7', column: 'Done' },
    { id: 8, name: 'task 8', column: 'Done' },
    { id: 9, name: 'task 9', column: 'Done' },
];

const Kanban = () => {
    const history = useHistory();
    const project = useRecoilValue(projectState);
    const [taskList, setTaskList] = useState<Array<TaskType>>(dummyTasks);

    const statuses = ['To Do', 'In Progress', 'Done'];
    const menu = ['로드맵', '백로그', '대시보드', '지도'];

    if (!project.id) {
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

    return (
        <>
            <Header />
            <SideBar props={menu} />
            <Container>
                <Wrapper>
                    {statuses.map((value) => (
                        <KanbanCol
                            key={value}
                            status={value}
                            tasks={taskList
                                .map((task, index) => ({ ...task, index }))
                                .filter((task) => task.column === value)}
                            changeColumn={changeTaskColumn}
                            moveTaskHandler={moveTaskHandler}
                        />
                    ))}
                    <KanbanAdditional>
                        <KanbanAddButton />
                    </KanbanAdditional>
                </Wrapper>
            </Container>
        </>
    );
};

export default Kanban;
