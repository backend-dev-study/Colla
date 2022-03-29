import React from 'react';
import { getRandomColor } from '../../../utils/common';
import { Container, Graph, GraphLine, RowText } from './style';

interface CountType {
    count: number;
}

interface DummyType {
    [key: string]: Array<CountType>;
}

const dummy: DummyType = {
    ToDo: [{ count: 5 }, { count: 2 }, { count: 8 }, { count: 12 }, { count: 1 }, { count: 3 }, { count: 3 }],
    InProgress: [{ count: 10 }, { count: 4 }, { count: 0 }, { count: 0 }, { count: 11 }, { count: 8 }, { count: 7 }],
    Done: [{ count: 0 }, { count: 1 }, { count: 5 }, { count: 3 }, { count: 2 }, { count: 7 }, { count: 10 }],
};

const LineChart = () => {
    let maxCount = 0;
    const width = screen.width * 0.375,
        height = screen.height * 0.33;
    const rowCnt = 8,
        columnCnt = 15;

    const calcDate = (i: number) => {
        const today = new Date();
        return new Date(today.setDate(today.getDate() - i)).getDate();
    };

    const drawSkeletonGraph = () => {
        const rows = Array(rowCnt)
            .fill(0)
            .map((el, i) => i)
            .map((i) => (
                <>
                    <GraphLine
                        key={`row${i}`}
                        x1={10}
                        y1={(height / (rowCnt - 1)) * i + 10}
                        x2={width + 10}
                        y2={(height / (rowCnt - 1)) * i + 10}
                    />
                    <RowText
                        key={`text${i}`}
                        dominantBaseline="baseline"
                        x={(width / (columnCnt - 1)) * i * 2}
                        y={height + 30}
                    >
                        {calcDate(rowCnt - i - 2)}
                    </RowText>
                </>
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

    const drawGraph = (status: string) => {
        const color = getRandomColor();
        let prevX = 0,
            prevY = 0;

        return Array(columnCnt)
            .fill(0)
            .map((el, i) => i)
            .filter((i) => i % 2 === 0)
            .map((i, idx) => {
                const x = (width / (columnCnt - 1)) * i + 10;
                const y = height;
                const max = height - 10;

                if (idx === 7) return null;

                const h = (max * dummy[status][idx].count) / maxCount;
                const graph =
                    prevX && prevY ? (
                        <>
                            <circle key={`circle ${i}`} cx={x} cy={y - h} r={5} fill={color} />
                            <line x1={prevX} y1={prevY} x2={x} y2={y - h} strokeWidth={2} stroke={color} />
                        </>
                    ) : (
                        <circle key={`circle ${i}`} cx={x} cy={y - h} r={5} fill={color} />
                    );

                prevX = x;
                prevY = y - h;

                return graph;
            });
    };

    const drawStatusGraph = () => {
        Object.values(dummy).forEach((status) => {
            status.forEach((el) => (maxCount = Math.max(maxCount, el.count)));
        });

        return Object.keys(dummy).map((status) => drawGraph(status));
    };

    return (
        <Container>
            <Graph>
                {drawSkeletonGraph()}
                {drawStatusGraph()}
            </Graph>
        </Container>
    );
};

export default LineChart;
