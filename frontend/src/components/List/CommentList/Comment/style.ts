import styled from '@emotion/styled';

interface Props {
    image?: string;
}

export const Container = styled.div`
    display: flex;
    flex-direction: row;
`;

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

export const ButtonContainer = styled.div`
    display: flex;
    flex-direction: row;
    font-size: 13px;

    div {
        margin-right: 5px;
        cursor: pointer;

        :hover {
            text-decoration: underline;
            font-weight: 800;
        }
    }
`;
