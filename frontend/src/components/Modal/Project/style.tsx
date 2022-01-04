import styled from '@emotion/styled';

export const Container = styled.div`
    position: absolute;
    align-items: center;
    width: 600px;
    height: 400px;
    font-size: 20px;
    border-radius: 40px;
    background: rgba(206, 232, 207);
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    padding: 40px 20px;
`;

export const ProjectNameInput = styled.input`
    font-size: 30px;
`;

export const ProjectDescInput = styled(ProjectNameInput)``;

export const SubmitButton = styled.div`
    padding: 10px 30px;
    border-radius: 20px;
    text-align: center;
    background: rgba(196, 196, 196);
`;
