import styled from '@emotion/styled';

export const Container = styled.div`
    position: absolute;
    display: flex;
    flex-direction: column;
    width: 400px;
    height: 200px;
    border-radius: 20px;
    margin: 100px 0 0 230px;
    background-color: #f1f1f1;
    box-shadow: 0 4px 4px rgba(0, 0, 0, 0.1), 0 4px 20px rgba(0, 0, 0, 0.1);
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
    border: none;
    resize: none;
    outline: none;
    margin: 10px 0 0 20px;
    padding: 10px 0 0 10px;
`;
