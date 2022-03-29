import React, { useEffect, useState } from 'react';

import { useLocation } from 'react-router-dom';
import { getTaskCountByManagerAndStatus, getTaskCountByStatus } from '../../apis/task';
import PieChart from '../../components/Chart/PieChart';
import ProgressBar from '../../components/Chart/ProgressBar';
import Template from '../../components/Template';
import { ManagerTaskCountType, TaskCountType } from '../../types/dashboard';
import { StateType } from '../../types/project';
import { Wrapper, LeftSide, RightSide, ProgressBarContainer, LineChartContainer, PieChartContainer } from './style';

const Dashboard = () => {
    const { state } = useLocation<StateType>();
    const [taskCountsByManager, setTaskCountsByManager] = useState<Array<ManagerTaskCountType>>([]);
    const [taskCountByStatus, setTaskCountByStatus] = useState<Array<TaskCountType>>([]);

    const handleUpdate = async () => {
        const managerResult = await getTaskCountByManagerAndStatus(state.projectId);
        setTaskCountsByManager(managerResult.data);
        const statusResult = await getTaskCountByStatus(state.projectId);
        setTaskCountByStatus(statusResult.data);
    };

    useEffect(() => {
        handleUpdate();
    }, []);

    return (
        <Template>
            <Wrapper>
                <LeftSide>
                    <ProgressBarContainer>
                        {taskCountsByManager.map((progress) => (
                            <ProgressBar
                                key={progress.managerName}
                                managerName={progress.managerName}
                                statuses={progress.taskCounts}
                            />
                        ))}
                    </ProgressBarContainer>
                </LeftSide>
                <RightSide>
                    <LineChartContainer />
                    <PieChartContainer>
                        <PieChart statuses={taskCountByStatus} />
                    </PieChartContainer>
                </RightSide>
            </Wrapper>
        </Template>
    );
};

export default Dashboard;
