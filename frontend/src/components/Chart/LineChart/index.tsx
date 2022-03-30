import React, { FC } from 'react';

import { getColorFromColorMap } from '../../../utils/common';
import { Container, Graph, GraphBorderLine, GraphLine } from './style';

interface CountType {
    count: number;
}

interface DummyType {
    [key: string]: Array<CountType>;
}

const dummy: DummyType = {
    'To Do': [{ count: 5 }, { count: 2 }, { count: 8 }, { count: 12 }, { count: 1 }, { count: 3 }, { count: 3 }],
    'In Progress': [{ count: 10 }, { count: 4 }, { count: 0 }, { count: 0 }, { count: 11 }, { count: 8 }, { count: 7 }],
    Done: [{ count: 0 }, { count: 1 }, { count: 5 }, { count: 3 }, { count: 2 }, { count: 7 }, { count: 10 }],
};

interface PropType {
    colors: Map<string, string>;
}

const LineChart: FC<PropType> = ({ colors }) => {
    let maxCount = 0;
    const width = window.innerWidth * 0.3,
        height = window.innerHeight * 0.3;
    const rowCnt = 8,
        columnCnt = 15;
    const offset = 12;

    const calcDate = (i: number) => {
        const today = new Date();
        return new Date(today.setDate(today.getDate() - i)).getDate();
    };

    const drawWeekDate = () => {
        const weekDate = Array(rowCnt)
            .fill(0)
            .map((el, i) => i)
            .map((i) => (
                <text
                    key={`text${i}`}
                    dominantBaseline="baseline"
                    x={(width / (columnCnt - 1)) * i * 2}
                    y={height + 30}
                >
                    {calcDate(rowCnt - i - 2)}
                </text>
            ));

        return weekDate;
    };

    const drawGraph = (status: string) => {
        const color = getColorFromColorMap(status, colors);
        let prevX = 0,
            prevY = 0;

        return Array(columnCnt)
            .fill(0)
            .map((el, i) => i)
            .filter((i) => i % 2 === 0)
            .map((i, idx) => {
                const x = (width / (columnCnt - 1)) * (idx < 7 ? i : i - 2) + offset;
                const y = height + offset;
                const max = height - offset;
                const h = (max * dummy[status][idx < 7 ? idx : idx - 1].count) / maxCount;

                if (idx === 7) {
                    return (
                        <text dominantBaseline="baseline" x={x + offset} y={y - h + 5} fill={color}>
                            {status}
                        </text>
                    );
                }

                const graph =
                    prevX && prevY ? (
                        <>
                            <circle cx={x} cy={y - h} r={5} fill={color} />
                            <GraphLine
                                x1={prevX}
                                y1={prevY}
                                x2={x}
                                y2={y - h}
                                stroke={color}
                                style={{ '--idx': idx } as React.CSSProperties}
                            />
                            <text dominantBaseline="baseline" x={x - 6} y={y - h - offset} fill={color}>
                                {dummy[status][idx].count}
                            </text>
                        </>
                    ) : (
                        <>
                            <circle cx={x} cy={y - h} r={5} fill={color} />
                            <text dominantBaseline="baseline" x={x - 6} y={y - h - offset} fill={color}>
                                {dummy[status][idx].count}
                            </text>
                        </>
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
                <GraphBorderLine x1={offset} y1={height + offset} x2={width + offset} y2={height + offset} />
                <GraphBorderLine x1={offset} y1={offset} x2={offset} y2={height + offset} />
                {drawWeekDate()}
                {drawStatusGraph()}
            </Graph>
        </Container>
    );
};

export default LineChart;
