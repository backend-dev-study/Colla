import styled from '@emotion/styled';

import { VIVID_GREEN } from '../../../styles/color';
import { List, Work } from '../../../styles/dropdown';

export const Container = styled.div`
    width: 100px;
    top: 30px;
    left: -30px;
    font-size: 16px;
    overflow-x: hidden;
    z-index: 10;

    ${List}
`;

export const Criteria = styled.div`
    width: 70px;

    ${Work}
`;

export const CriteriaTitle = styled.div`
    color: ${(props: { selected: boolean }) => (props.selected ? VIVID_GREEN : 'initial')};
`;
