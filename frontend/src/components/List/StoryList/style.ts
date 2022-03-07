import styled from '@emotion/styled';
import { LIGHT_GRAY } from '../../../styles/color';
import { Column, LiftUp } from '../../../styles/common';

export const Title = styled.div`
    font-size: 20px;
    margin: 30px 0 0 30px;

    &:hover {
        cursor: pointer;
        text-decoration: 3px solid #000 underline;
    }
`;

export const List = styled.div`
    align-items: center;
    justify-content: center;

    ${Column}
`;

export const Issue = styled.div`
    justify-content: space-around;
    width: 250px;
    min-height: 70px;
    border-radius: 20px;
    margin: 20px 0 0 0;
    background-color: ${LIGHT_GRAY};
    cursor: pointer;

    ${Column}
    ${LiftUp}
`;

export const IssueContents = styled.div`
    padding: 10px 10px 10px 20px;
`;
