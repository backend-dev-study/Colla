import styled from '@emotion/styled';

interface PropType {
    percent: number;
    color: string;
}

export const Wrapper = styled.div`
    display: flex;
    width: 100%;
    justify-content: space-around;
    margin-top: 10px;
    margin-bottom: 15px;
`;

export const Manager = styled.div`
    margin-left: 5px;
    margin-right: 5px;
`;

export const PartialHover = styled.div`
    position: absolute;
    top: -15px;
    left: 5px;
    width: 100px;
    height: 30px;
    visibility: hidden;
`;

export const PartialBar = styled.div<PropType>`
    position: relative;
    width: ${({ percent }) => `${percent}%`};
    height: 30px;
    background: ${({ color }) => color};

    &:hover ${PartialHover} {
        visibility: visible;
    }
    :hover {
        animation: scale-up 0.3s forwards;
    }

    @keyframes scale-up {
        0% {
            transform: scaleY(100%);
        }
        100% {
            transform: scaleY(120%);
        }
    }
`;

export const Bar = styled.div`
    display: flex;
    width: 100%;
    ${PartialBar}:first-child {
        border-radius: 10px 0 0 10px;
    }
    ${PartialBar}:last-child {
        border-radius: 0 10px 10px 0;
    }
`;
