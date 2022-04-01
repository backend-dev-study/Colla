import React, { FC, useEffect, useState } from 'react';

import { useLocation } from 'react-router-dom';
import { getWeeklyTaskStatusLog } from '../../../apis/project';
import { StateType, WeeklyTaskStatusLogType } from '../../../types/project';
import { getColorFromColorMap, isSameDate } from '../../../utils/common';
import { Container, Graph, GraphBorderLine, GraphLine } from './style';

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
    const { state } = useLocation<StateType>();
    const weeklyDate: Array<Date> = [];
    const [weeklyTaskStatusLog, setWeeklyTaskStatusLog] = useState<WeeklyTaskStatusLogType>({});

    const calcDate = () => {
        Array(rowCnt)
            .fill(0)
            .map((el, i) => i)
            .map((i) => rowCnt - i - 2)
            .forEach((i) => {
                const today = new Date();
                weeklyDate.push(new Date(today.setDate(today.getDate() - i)));
            });
    };

    const drawWeeklyDate = () => {
        calcDate();
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
                    {weeklyDate[i].getDate()}
                </text>
            ));

        return weekDate;
    };

    const drawGraph = (status: string) => {
        const color = getColorFromColorMap(status, colors);
        let prevX = 0,
            prevY = 0,
            prevH = 0;

        return Array(columnCnt)
            .fill(0)
            .map((el, i) => i)
            .filter((i) => i % 2 === 0)
            .map((i, idx) => {
                const dailyCount = weeklyTaskStatusLog[status].filter((el) =>
                    isSameDate(new Date(el.createdAt), weeklyDate[idx]),
                );
                const count = dailyCount.length > 0 ? dailyCount[0].count : 0;
                const x = (width / (columnCnt - 1)) * (idx < 7 ? i : i - 2) + offset;
                const y = height + offset;
                const max = height - offset;
                const h = (max * count) / maxCount;

                if (idx === 7) {
                    return (
                        <text dominantBaseline="baseline" x={x + offset} y={y - prevH + 5} fill={color}>
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
                                {count}
                            </text>
                        </>
                    ) : (
                        <>
                            <circle cx={x} cy={y - h} r={5} fill={color} />
                            <text dominantBaseline="baseline" x={x - 6} y={y - h - offset} fill={color}>
                                {count}
                            </text>
                        </>
                    );

                prevX = x;
                prevY = y - h;
                prevH = h;

                return graph;
            });
    };

    const drawStatusGraph = () => {
        Object.values(weeklyTaskStatusLog).forEach((statusLogs) => {
            statusLogs.forEach((el) => (maxCount = Math.max(maxCount, el.count)));
        });

        return Object.keys(weeklyTaskStatusLog).map((status) => drawGraph(status));
    };

    useEffect(() => {
        (async () => {
            const res = await getWeeklyTaskStatusLog(state.projectId);
            setWeeklyTaskStatusLog(res.data);
        })();
    }, []);

    return (
        <Container>
            <Graph>
                <GraphBorderLine x1={offset} y1={height + offset} x2={width + offset} y2={height + offset} />
                <GraphBorderLine x1={offset} y1={offset} x2={offset} y2={height + offset} />
                {drawWeeklyDate()}
                {drawStatusGraph()}
            </Graph>
        </Container>
    );
};

export default LineChart;
