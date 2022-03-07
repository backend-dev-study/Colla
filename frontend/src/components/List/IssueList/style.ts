import styled from '@emotion/styled';
import { GREEN } from '../../../styles/color';

export const Container = styled.div`
    width: 300px;
    height: 600px;
    border-radius: 20px;
    margin-left: auto;
    background-color: ${GREEN};
    overflow-y: scroll;

    &::-webkit-scrollbar {
        width: 8px;
    }

    &::-webkit-scrollbar-thumb {
        background: #bbbbbb;
        border-radius: 10px;
    }
`;

export const Title = styled.div`
    font-size: 20px;
    margin: 30px 0 0 30px;
`;

export const Story = styled.div``;
