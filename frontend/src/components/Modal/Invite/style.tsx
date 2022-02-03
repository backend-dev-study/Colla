import styled from '@emotion/styled';
import { GRAY, GREEN } from '../../../styles/color';
import { Center, Column, Modal } from '../../../styles/common';

export const Wrapper = styled.div`
    position: absolute;
    justify-content: space-around;
    width: 500px;
    height: 300px;
    font-size: 20px;
    border-radius: 40px;
    background: ${GRAY};
    padding: 40px 20px;

    top: 10px;

    ${Modal};
`;

export const InviteUser = styled.div`
    display: flex;
    justify-content: space-around;
`;

export const InviteEmailInput = styled.input`
    width: 300px;
    height: 35px;
    border-radius: 10px;
    font-size: 20px;
`;

export const InviteButton = styled.button`
    width: 80px;
    height: 35px;
    border-radius: 10px;
    background: ${GREEN};
    font-size: 20px;
`;

export const Member = styled.div`
    display: flex;
    justify-content: space-around;
`;

export const MemberInfo = styled.div`
    width: 300px;
    height: 70px;
    justify-content: space-around;

    ${Column}
`;

export const MemberImage = styled.div``;

export const ImageContainer = styled.div`
    background: url(${(props: { image: string }) => props.image});
    background-size: cover;
    width: 70px;
    height: 70px;
    font-size: 20px;
    border-radius: 100px;
    border: none;

    ${Center}
`;

export const MemberList = styled.div`
    overflow-y: scroll;
    &::-webkit-scrollbar {
        display: none;
    }
`;
