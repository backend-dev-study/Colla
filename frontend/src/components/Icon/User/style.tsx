import styled from '@emotion/styled';
import { Center } from '../../../styles/common';

interface Props {
    image?: string;
    size: 'big' | 'small';
}

export const Container = styled.div<Props>`
    width: ${({ size }) => (size === 'big' ? '160px' : '70px')};
    height: ${({ size }) => (size === 'big' ? '160px' : '70px')};
    font-size: 20px;
    border-radius: 100px;
    border: none;
    background: rgba(196, 196, 196);
    :hover {
        opacity: 50%;
        cursor: pointer;
    }

    ${Center}
`;

export const ImageContainer = styled(Container)<Props>`
    background: url(${({ image }) => image});
`;

export const Icon = styled.div`
    position: relative;
`;
