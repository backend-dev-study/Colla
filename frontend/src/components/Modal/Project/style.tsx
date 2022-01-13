import styled from '@emotion/styled';
import { GRAY, GREEN } from '../../../styles/color';
import { Modal } from '../../../styles/common';

export const Container = styled.div`
    position: absolute;
    justify-content: space-around;
    align-items: center;
    width: 600px;
    height: 400px;
    font-size: 20px;
    border-radius: 40px;
    background: ${GREEN};
    padding: 40px 20px;

    ${Modal}
`;

export const ProjectNameInput = styled.input`
    font-size: 30px;
    border-radius: 10px;
`;

export const ProjectDescInput = styled(ProjectNameInput)``;

export const SubmitButton = styled.button`
    padding: 10px 30px;
    border-radius: 10px;
    text-align: center;
    background: ${GRAY};
    font-size: 20px;
`;
