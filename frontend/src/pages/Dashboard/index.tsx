import React from 'react';

import PieChart from '../../components/Chart/PieChart';
import Template from '../../components/Template';
import { Wrapper, LeftSide, RightSide, ProgressBarContainer, LineChartContainer, PieChartContainer } from './style';

const Dashboard = () => (
    <Template>
        <Wrapper>
            <LeftSide>
                <ProgressBarContainer />
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
