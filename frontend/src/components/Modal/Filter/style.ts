import styled from '@emotion/styled';
import { GRAY, WHITE } from '../../../styles/color';

export const Container = styled.div`
    position: absolute;
    width: 300px;
    top: 210px;
    border: 2px solid ${GRAY};
    border-radius: 10px;
    background-color: ${WHITE};
    z-index: 10;
`;

export const FilterCriteria = styled.div`
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    height: 30px;
    font-size: 17px;
    margin-top: 10px;
    border-bottom: 1px solid #000;
    color: ${GRAY};

    div {
        cursor: pointer;

        &.selected {
            color: #000;
            border-bottom: 2px solid #000;
        }
    }
`;

export const CriteriaElements = styled.div`
    padding: 10px 25px 0 25px;
    font-size: 16px;
`;

export const Element = styled.div`
    display: flex;
    flex-direction: row;
    margin-bottom: 10px;

    :hover {
        cursor: pointer;
    }
`;

export const CheckMark = styled.img`
    margin-left: auto;
`;
