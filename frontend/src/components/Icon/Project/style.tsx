import styled from '@emotion/styled';
import { GRAY } from '../../../styles/color';
import { Center } from '../../../styles/common';

interface Props {
    image: string;
}

export const Container = styled.div`
    width: 50px;
    height: 50px;
    font-size: 30px;
    border-radius: 20px;
    background: ${GRAY};

    ${Center}
`;

export const ImageContainer = styled(Container)<Props>`
    background: url(${({ image }) => image});
`;
