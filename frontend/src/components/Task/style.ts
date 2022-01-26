import styled from '@emotion/styled';
import { GREEN } from '../../styles/color';

export const Wrapper = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    width: 250px;
    min-height: 50px;
    border: 3px solid ${GREEN};
    border-radius: 10px;
    margin: 10px 10px;
    padding: 15px;
`;

export const Title = styled.div`
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
`;

export const TaskTitle = styled.div`
    max-width: 200px;
`;

export const Priority = styled.div`
    margin-top: -2px;
`;

export const Star = styled.img`
    width: 10px;
    height: 10px;
`;

export const Manager = styled.div`
    display: flex;
`;

export const Avatar = styled.img`
    width: 20px;
    height: 20px;
    object-fit: cover;
    border-radius: 10px;
`;

export const Name = styled.span`
    margin: 2px 0px 0px 5px;
`;
