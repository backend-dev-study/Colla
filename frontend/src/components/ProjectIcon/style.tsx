import { css } from '@emotion/react';
import styled from '@emotion/styled';

interface Props {
    image: string;
}

const flexStyle = css`
    display: flex;
    justify-content: center;
`;

export const Container = styled.div`
    width: 100px;
    height: 100px;
    font-size: 60px;
    border-radius: 20px;
    border: 1px solid;
    align-items: center;
    background: whitesmoke;
    ${flexStyle}
`;

export const ImageContainer = styled(Container)<Props>`
    background: url(${({ image }) => image});
`;
