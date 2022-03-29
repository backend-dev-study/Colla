import React from 'react';
import { Container, Graph, GraphLine } from './style';

const LineChart = () => {
    const drawSkeletonGraph = () => {
        const width = screen.width * 0.375,
            height = screen.height * 0.33;
        const rowCnt = 7,
            columnCnt = 13;

        const rows = Array(rowCnt)
            .fill(0)
            .map((el, i) => i)
            .map((i) => (
                <GraphLine
                    key={`row${i}`}
                    x1={10}
                    y1={(height / (rowCnt - 1)) * i + 10}
                    x2={width + 10}
                    y2={(height / (rowCnt - 1)) * i + 10}
                />
            ));

        const columns = Array(columnCnt)
            .fill(0)
            .map((el, i) => i)
            .map((i) => (
                <GraphLine
                    key={`col${i}`}
                    x1={(width / (columnCnt - 1)) * i + 10}
                    y1={10}
                    x2={(width / (columnCnt - 1)) * i + 10}
                    y2={height + 10}
                />
            ));

        return [...rows, ...columns];
    };

    return (
        <Container>
            <Graph>{drawSkeletonGraph()}</Graph>
        </Container>
    );
};

export default LineChart;
