import React from 'react';

import LineChart from '../../components/Chart/LineChart';
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

const dummyTotalTaskStatuses = [
    { statusName: 'to do', statusCounts: 63, total: 194 },
    { statusName: 'progress', statusCounts: 30, total: 194 },
    { statusName: 'done', statusCounts: 57, total: 194 },
    { statusName: 'unknown', statusCounts: 27, total: 194 },
    { statusName: 'new', statusCounts: 17, total: 194 },
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
                <LineChartContainer>
                    <LineChart />
                </LineChartContainer>
                <PieChartContainer>
                    <PieChart statuses={dummyTotalTaskStatuses} />
                </PieChartContainer>
            </RightSide>
        </Wrapper>
    </Template>
);

export default Dashboard;
