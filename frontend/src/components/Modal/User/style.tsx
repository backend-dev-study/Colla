import styled from '@emotion/styled';
import { GRAY, GREEN } from '../../../styles/color';
import { Modal } from '../../../styles/common';

export const Container = styled.div`
    position: absolute;
    top: 10px;
    width: 380px;
    justify-content: space-around;
    height: 250px;
    font-size: 20px;
    right: -70px;
    background: ${GREEN};
    border-radius: 20px;
    padding: 40px 0 40px 20px;

    ${Modal}
`;

export const DisplayName = styled.div`
    height: 30px;
    margin-right: 10px;
    min-width: 132px;
`;

export const UserName = styled.div`
    height: 27px;
`;

export const GithubId = styled.div``;

export const LogoutButton = styled.button`
    padding: 4px 0px;
    height: 30px;
    font-size: 16px;
    border-radius: 10px;
    text-align: center;
    background: ${GRAY};
    margin-right: 20px;
    cursor: pointer;
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
    min-width: 30px;
`;

export const Cancel = styled.span`
    margin: 0 20px 0 10px;
    font-size: 16px;
    cursor: pointer;
    min-width: 30px;
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
