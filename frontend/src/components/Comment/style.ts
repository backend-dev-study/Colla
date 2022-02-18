import styled from '@emotion/styled';
import { WHITE } from '../../styles/color';
import { LiftUp } from '../../styles/common';

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

export const ContentsInputContainer = styled.div`
    display: flex;
    flex-direction: row;
    min-width: 420px;

    div {
        width: 40px;
        margin: 10px 0 0 10px;
        cursor: pointer;

        ${LiftUp}
    }
`;

export const ContentsInput = styled.input`
    background-color: transparent;
    margin-bottom: 5px;
    width: 390px;
    height: 20px;

    &.modifying {
        height: 40px;
        background-color: ${WHITE};
        border: none;
        border-radius: 10px;
        padding-left: 10px;
        font-size: 15px;
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
