import styled from '@emotion/styled';

export const Container = styled.div`
    display: flex;
    flex-direction: column;
    width: 530px;
    margin-left: 3px;
    max-height: 250px;
    overflow-y: scroll;

    &::-webkit-scrollbar {
        width: 8px;
    }

    &::-webkit-scrollbar-thumb {
        background: #bbbbbb;
        border-radius: 10px;
    }
`;

export const SuperCommentContainer = styled.div`
    display: flex;
    flex-direction: column;
    margin-top: 15px;
`;

export const SuperComment = styled.div`
    display: flex;
    flex-direction: row;
    margin-bottom: 7px;
`;

export const SubComment = styled.div`
    display: flex;
    flex-direction: row;
    margin: 0 0 7px 45px;
`;

interface Props {
    image?: string;
}

export const Writer = styled.div<Props>`
    background: url(${({ image }) => image});
    background-size: cover;
    width: 35px;
    height: 35px;
    border-radius: 50%;
`;

export const Contents = styled.div`
    display: flex;
    flex-direction: column;
    margin-left: 15px;
    max-width: 360px;

    span {
        margin-bottom: 5px;
    }

    span:last-child {
        font-size: 14px;
    }
`;
