import styled from '@emotion/styled';
import { ModalContainer } from '../../../styles/modal';

export const Container = styled.div`
    width: 400px;
    height: 200px;
    margin: 130px 0 0 235px;

    ${ModalContainer}
`;

export const Story = styled.div`
    display: flex;
    flex-direction: column;

    span {
        margin: 20px 0 0 20px;
    }
`;

export const StoryArea = styled.textarea`
    width: 350px;
    height: 80px;
    font-size: 15px;
    border-radius: 10px;
    margin: 10px 0 0 20px;
    padding: 10px 0 0 10px;
`;
