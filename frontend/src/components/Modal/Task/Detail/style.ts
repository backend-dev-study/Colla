import styled from '@emotion/styled';
import { GRAY } from '../../../../styles/color';

export const DetailContainer = styled.div`
    display: flex;
    flex-direction: column;
    width: 260px;
    margin: 30px 0 0 40px;
    border-radius: 20px;
    background-color: ${GRAY};
`;

export const DetailComponent = styled.div`
    display: flex;
    flex-direction: column;
    margin: 20px 0 0 20px;
`;

export const MemberList = styled.div`
    display: flex;
    align-items: center;
    width: 200px;
    min-height: 25px;
    border: 1px solid #000;
    border-radius: 10px;
    margin-top: 10px;
    padding: 5px 0 5px 10px;
    background-color: transparent;
    cursor: pointer;
`;

export const Status = styled.div`
    margin-top: 10px;
`;
