import styled from '@emotion/styled';

export const Hover = styled.div`
    position: absolute;
    visibility: hidden;
    top: 20px;
    left: 15px;
    white-space: nowrap;
`;

export const Text = styled.div`
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
`;

export const Wrapper = styled.div`
    width: 100%;
    position: relative;

    &:hover ${Hover} {
        visibility: visible;
    }
`;
