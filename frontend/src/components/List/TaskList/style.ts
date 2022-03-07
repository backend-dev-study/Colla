import styled from '@emotion/styled';
import { GREEN } from '../../../styles/color';

export const Title = styled.div`
    font-size: 20px;
    margin: 30px 0 0 30px;

    &:hover {
        cursor: pointer;
        text-decoration: 3px solid #000 underline;
    }
`;

export const Tag = styled.div`
    display: inline-block;
    margin: 3px 7px 5px 0;
    padding: 0 5px;
    border-radius: 5px;
    cursor: pointer;
    background: ${GREEN};
`;
