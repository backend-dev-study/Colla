import React from 'react';

import PieChart from '../../components/Chart/PieChart';
import ProgressBar from '../../components/Chart/ProgressBar';
import Template from '../../components/Template';
import { Wrapper, LeftSide, RightSide, ProgressBarContainer, LineChartContainer, PieChartContainer } from './style';

const dummyTaskProgresses = [
    {
        manager: 'SB',
        tasks: [
            { statusName: 'to do', statusCounts: 63, total: 100 },
            { statusName: 'progress', statusCounts: 30, total: 100 },
            { statusName: 'done', statusCounts: 7, total: 100 },
        ],
    },
    {
        manager: 'YG',
        tasks: [
            { statusName: 'to do', statusCounts: 43, total: 100 },
            { statusName: 'progress', statusCounts: 43, total: 100 },
            { statusName: 'done', statusCounts: 121, total: 207 },
        ],
    },
];

const Dashboard = () => (
    <Template>
        <Wrapper>
            <LeftSide>
                <ProgressBarContainer>
                    {dummyTaskProgresses.map((progress) => (
                        <ProgressBar key={progress.manager} managerName={progress.manager} statuses={progress.tasks} />
                    ))}
                </ProgressBarContainer>
            </LeftSide>
            <RightSide>
                <LineChartContainer />
                <PieChartContainer>
                    <PieChart />
                </PieChartContainer>
            </RightSide>
        </Wrapper>
    </Template>
);

export default Dashboard;
