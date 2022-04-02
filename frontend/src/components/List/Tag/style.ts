import styled from '@emotion/styled';

export const Container = styled.div`
    display: flex;
    max-width: 240px;
`;

export const Grid = styled.div`
    width: 100%;
    max-width: 240px;
`;

export const Tag = styled.div`
    display: inline-block;
    margin: 3px 7px 5px 0;
    padding: 5px;
    border: 1px solid #000;
    border-radius: 10px;
    cursor: pointer;

    :hover {
        color: #fff;
        background-color: #000;
    }

    &.selected {
        color: #fff;
        background-color: #000;
    }
`;
