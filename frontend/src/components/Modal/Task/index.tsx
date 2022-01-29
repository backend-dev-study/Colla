import React, { FC } from 'react';

import { TaskType } from '../../../types/kanban';
import { BasicInfoContainer } from './Basic';
import { DetailInfoContainer } from './Detail';
import { Container, ModalContainer, CancelButton, CompleteButton, ButtonContainer } from './style';

interface PropType {
    status: string;
    taskList: TaskType[];
    hideModal: Function;
}

export const TaskModal: FC<PropType> = ({ status, taskList, hideModal }) => (
    <ModalContainer>
        <Container>
            <BasicInfoContainer taskList={taskList} />
            <DetailInfoContainer status={status} />
        </Container>
        <ButtonContainer>
            <CancelButton onClick={() => hideModal()}>취소</CancelButton>
            <CompleteButton>완료</CompleteButton>
        </ButtonContainer>
    </ModalContainer>
);
