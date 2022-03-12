import styled from '@emotion/styled';
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

export const Temp = styled.div`
    width: 49%;
    border: 2px solid black;
`;
