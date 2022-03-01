import styled from '@emotion/styled';
import { List, Work } from '../../../styles/dropdown';

export const Container = styled.div`
    width: 92px;
    top: 30px;
    left: -30px;
    font-size: 16px;
    overflow-x: hidden;
    z-index: 10;

    ${List}
`;

export const Criteria = styled.div`
    width: 60px;

    ${Work}
`;
