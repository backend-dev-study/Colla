import styled from '@emotion/styled';
import { GREEN, LIGHT_GRAY } from '../../styles/color';
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
