import styled from '@emotion/styled';

export const Container = styled.div`
    position: absolute;
    margin-top: calc(50vh - 250px);
    margin-left: calc(50vw - 300px);
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
export const DisplayName = styled.input`
    font-size: 30px;
`;
export const Description = styled(DisplayName)``;
export const LogOutButton = styled.div`
    padding: 10px 30px;
    border-radius: 20px;
    text-align: center;
    background: rgba(196, 196, 196);
`;

export const ProjectIcon = styled.div`
    width: 200px;
    height: 200px;
    border-radius: 100%;
    background: rgba(196, 196, 196);
`;
