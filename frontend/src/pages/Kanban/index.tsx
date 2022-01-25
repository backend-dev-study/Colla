import React, { useEffect, useState } from 'react';
import { useHistory, useLocation } from 'react-router-dom';

import PlusIcon from '../../../public/assets/images/plus-circle.svg';
import { getProject } from '../../apis/project';
import Header from '../../components/Header';
import KanbanCol from '../../components/KanbanCol';
import { SideBar } from '../../components/SideBar';
import { TaskType } from '../../types/kanban';
import { Wrapper, KanbanAddButton, KanbanAdditional, Container } from './style';

interface stateType {
    projectId: number;
}

const Kanban = () => {
    const history = useHistory();
    const { state } = useLocation<stateType>();
    const [taskList, setTaskList] = useState<Array<TaskType>>([]);

    const statuses = ['To Do', 'In Progress', 'Done'];
    const menu = ['로드맵', '백로그', '대시보드', '지도'];

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
            const { tasks } = res.data;
            setTaskList([
                ...tasks['To Do'].map((task) => ({ ...task, column: 'To Do' })),
                ...tasks['In Progress'].map((task) => ({ ...task, column: 'In Progress' })),
                ...tasks['Done'].map((task) => ({ ...task, column: 'Done' })),
            ]);
        })();
    }, []);

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
                        <KanbanAddButton src={PlusIcon} />
                    </KanbanAdditional>
                </Wrapper>
            </Container>
        </>
    );
};

export default Kanban;
