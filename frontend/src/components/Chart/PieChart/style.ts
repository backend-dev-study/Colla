import styled from '@emotion/styled';

interface PropType {
    color: string;
}

export const Svg = styled.svg`
    width: 100%;
    height: 100%;
`;

export const Colors = styled.div`
    display: flex;
    align-items: center;
`;

export const StatusName = styled.div`
    width: 100px;
    text-align: center;
`;

export const Color = styled.div<PropType>`
    width: 50px;
    height: 30px;
    background: ${({ color }) => color};
    margin: 0 10px 0 10px;
`;
