import styled from '@emotion/styled';

export const Container = styled.div`
    position: absolute;
    margin-top: 60px;
    right: 10px;
    width: 300px;
    height: 200px;
    font-size: 20px;
    border-radius: 20px;
    background: rgba(206, 232, 207);
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    padding: 40px 20px;
`;
export const DisplayName = styled.div``;
export const GithubId = styled.div``;
export const LogOutButton = styled.div`
    padding: 4px 0px;
    border-radius: 20px;
    text-align: center;
    background: rgba(196, 196, 196);
`;
export const Edit = styled.img`
    width: 20px;
    height: 20px;
    :hover {
        opacity: 50%;
    }
`;
export const Wrapper = styled.div`
    display: flex;
`;
