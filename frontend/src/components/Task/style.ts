import styled from '@emotion/styled';
import { GREEN } from '../../styles/color';

export const Wrapper = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    width: 280px;
    height: 50px;
    border: 3px solid ${GREEN};
    border-radius: 10px;
    margin: 10px 10px;
`;
