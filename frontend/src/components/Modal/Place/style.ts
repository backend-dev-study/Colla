import styled from '@emotion/styled';

import { GRAY, LIGHT_GRAY } from '../../../styles/color';
import { Column, Modal } from '../../../styles/common';

export const Wrapper = styled.div`
    align-items: center;
    width: 600px;
    height: 400px;
    border-radius: 20px;
    background: ${GRAY};

    overflow-y: scroll;

    ${Modal}
`;

export const SearchInput = styled.input`
    width: 400px;
    line-height: 30px;
    font-size: 24px;
    border-radius: 10px;
    margin-top: 10px;
    margin-bottom: 10px;
`;

export const SearchList = styled.div`
    align-items: center;

    ${Column}
`;

export const Place = styled.div`
    width: 400px;
    height: 120px;
    border-radius: 20px;
    margin: 5px 10px 5px 10px;
    padding: 10px;
    background: ${LIGHT_GRAY};
`;
