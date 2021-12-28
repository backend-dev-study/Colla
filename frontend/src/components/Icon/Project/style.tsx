import styled from '@emotion/styled';
import { Center } from '../../../styles/common';
interface Props {
    image: string;
}

export const Container = styled.div`
    width: 100px;
    height: 100px;
    font-size: 60px;
    border-radius: 20px;
    border: 1px solid;
    background: rgba(196, 196, 196);
    ${Center}
    :hover {
        opacity: 50%;
    }
`;

export const ImageContainer = styled(Container)<Props>`
    background: url(${({ image }) => image});
`;
