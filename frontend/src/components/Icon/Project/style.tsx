import styled from '@emotion/styled';
import { Center } from '../../../styles/common';

export const Container = styled.div`
    width: 70px;
    height: 70px;
    font-size: 30px;
    border-radius: 10px;

    ${Center}
`;

export const FileInput = styled.input`
    display: none;
`;

export const Image = styled.img`
    width: 70px;
    height: 70px;
    border-radius: 10px;
    cursor: pointer;
    object-fit: cover;
`;
