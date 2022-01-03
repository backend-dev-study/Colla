import styled from '@emotion/styled';

export const Container = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    position: absolute;
    width: 400px;
    height: 250px;
    margin-top: 130px;
    right: 50px;
    font-size: 20px;
    border-radius: 20px;
    background: rgba(206, 232, 207);
    padding: 40px 0 40px 20px;
`;

export const DisplayName = styled.div`
    height: 30px;
    margin-right: 10px;
`;

export const UserName = styled.div`
    height: 27px;
`;

export const GithubId = styled.div``;

export const LogoutButton = styled.div`
    padding: 4px 0px;
    border-radius: 20px;
    text-align: center;
    background: rgba(196, 196, 196);
    margin-right: 20px;
`;

export const Edit = styled.img`
    width: 20px;
    height: 20px;
    :hover {
        opacity: 50%;
        cursor: pointer;
    }
    margin-left: 10px;
`;

export const Complete = styled.span`
    margin-left: 10px;
    font-size: 16px;
    cursor: pointer;
`;

export const Cancel = styled.span`
    margin: 0 20px 0 10px;
    font-size: 16px;
    cursor: pointer;
`;

export const Wrapper = styled.div`
    display: flex;
`;

export const InputBar = styled.input`
    border: none;
    width: 130px;
    height: 25px;
    border-radius: 10px;
    font-size: 20px;
    padding-left: 10px;
    margin-top: -4px;
`;
