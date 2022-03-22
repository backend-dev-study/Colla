import React from 'react';

import Template from '../../components/Template';
import { Wrapper, LeftSide, RightSide, ProgressBar, LineChart, PieChart } from './style';

const Dashboard = () => (
    <Template>
        <Wrapper>
            <LeftSide>
                <ProgressBar />
            </LeftSide>
            <RightSide>
                <LineChart />
                <PieChart />
            </RightSide>
        </Wrapper>
    </Template>
);

export default Dashboard;
