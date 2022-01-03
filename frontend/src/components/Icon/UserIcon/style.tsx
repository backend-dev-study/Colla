import styled from '@emotion/styled';
import { Center, WidthCenter } from '../../../styles/common';

interface Props {
    image?: string;
    size: 'big' | 'small';
}

export const Container = styled.div<Props>`
    width: ${({ size }) => (size === 'big' ? '160px' : '50px')};
    height: ${({ size }) => (size === 'big' ? '160px' : '50px')};
    font-size: 30px;
    border-radius: 100px;
    background: rgba(196, 196, 196);
    ${Center}
    :hover {
        opacity: 50%;
    }
`;

export const ImageContainer = styled(Container)<Props>`
    background: url(${({ image }) => image});
`;

export const Modal = styled.div`
    position: absolute;
    top: 100px;
`;

export const Icon = styled.div`
    position: relative;
    ${WidthCenter}
`;
