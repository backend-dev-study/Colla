import styled from '@emotion/styled';

import { ROADMAP_DATES_LIMIT } from '../../constants';
import { GRAY, GREEN, LIGHT_GRAY } from '../../styles/color';
import { Column } from '../../styles/common';

export const Container = styled.div`
    align-items: center;

    ${Column}
`;

export const Wrapper = styled.div`
    display: flex;
    justify-content: space-between;
    width: 80vw;
    height: 600px;
    border-radius: 20px;
    background: ${LIGHT_GRAY};
    margin-top: 50px;
`;

export const RoadmapArea = styled.div`
    width: 65%;
`;

export const Grid = styled.div`
    display: grid;
    grid-template-columns: 70px repeat(${ROADMAP_DATES_LIMIT}, 1fr);
    grid-template-rows: 50px;
`;

export const ListArea = styled.div`
    width: 300px;
    height: 590px;
    border-radius: 20px;
    background-color: ${GREEN};
    overflow-y: scroll;
    padding-bottom: 10px;

    &::-webkit-scrollbar {
        width: 8px;
    }

    &::-webkit-scrollbar-thumb {
        background: #bbbbbb;
        border-radius: 10px;
    }
`;

export const RoadmapDate = styled.div`
    display: flex;
    min-width: 50px;
    border-right: solid 1px ${GRAY};
    align-items: center;

    &:first-of-type {
        grid-column-start: 2;
        border-left: solid 1px ${GRAY};
    }
`;
