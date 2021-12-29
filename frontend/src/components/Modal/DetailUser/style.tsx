import styled from '@emotion/styled';

export const Container = styled.div`
    margin-top: calc(50vh - 250px);
    margin-left: calc(50vw - 300px);
    position: absolute;
    align-items: center;
    width: 600px;
    height: 400px;
    font-size: 20px;
    border-radius: 40px;
    border: 1px solid;
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
    border: 1px solid;
    text-align: center;
    background: rgba(196, 196, 196);
`;
