import styled from '@emotion/styled';
import { Center, LiftUp } from '../../styles/common';

export const Container = styled.div`
    flex-direction: column;
    align-items: center;

    ${Center}
`;

export const FeatureContainer = styled.div`
    display: flex;
    width: 80vw;
    height: 50px;
    margin-top: 30px;
    justify-content: right;
`;

export const Features = styled.div`
    display: flex;
    width: 600px;
    justify-content: flex-end;
    align-items: center;
`;

export const Feature = styled.div`
    position: relative;
    font-size: 20px;
    margin-right: 30px;

    span:hover {
        cursor: pointer;
        text-decoration: 3px solid #000 underline;
    }
`;

export const SearchBar = styled.div`
    display: flex;
    flex-direction: row;
`;

export const SearchInput = styled.input`
    width: 300px;
    height: 40px;
    border: 2px solid #000;
    border-radius: 16px;
    font-size: 15px;
    padding-left: 15px;
    background: transparent;
`;

export const SearchIcon = styled.img`
    width: 24px;
    height: 24px;
    margin: 10px 0 0 -35px;

    ${LiftUp}
`;
