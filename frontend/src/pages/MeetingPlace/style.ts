import styled from '@emotion/styled';

import { GRAY, LIGHT_GRAY } from '../../styles/color';
import { Column } from '../../styles/common';

export const Container = styled.div`
    align-items: center;
    margin-top: 70px;
    ${Column}
`;

export const Wrapper = styled.div`
    display: flex;
    width: 80vw;
    height: 500px;
    border-radius: 20px;
    justify-content: space-between;
`;

export const PlaceContainer = styled.div`
    position: relative;
    align-items: center;
    height: 680px;

    ${Column}
`;

export const AddPlace = styled.button`
    width: 150px;
    height: 50px;
    position: relative;
    right: -40%;
    margin-top: 10px;
    margin-bottom: 20px;
    background: ${LIGHT_GRAY};
    border-radius: 20px;

    &:hover {
        opacity: 0.3;
    }
`;

export const PlaceList = styled.div`
    align-items: center;
    width: 38vw;
    min-width: fit-content;
    height: 600px;
    overflow-y: scroll;
    background: ${LIGHT_GRAY};
    border-radius: 20px;
    padding: 10px;

    &::-webkit-scrollbar {
        display: none;
    }

    ${Column}
`;

export const Place = styled.div`
    width: 400px;
    min-height: 150px;
    border-radius: 20px;
    margin: 5px 10px 5px 10px;
    padding: 10px;
    background: ${GRAY};
`;
