import styled from '@emotion/styled';
import { Center } from '../../styles/common';

export const Container = styled.div`
    flex-direction: column;
    align-items: center;
    ${Center}
`;

export const FeatureContainer = styled.div`
    display: flex;
    flex-direction: row;
    width: 500px;
    height: 50px;
    margin: 50px 0 10px 600px;
    align-items: center;
`;

export const TaskContainer = styled.div`
    width: 1200px;
    height: 500px;
    background-color: #c5f0f5;
`;

export const Feature = styled.div`
    font-size: 20px;
    margin-right: 30px;
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
`;
