import React, { FC } from 'react';

import useInputTask from '../../../hooks/useInputTask';
import { TaskType } from '../../../types/kanban';
import { BasicInfoContainer } from './Basic';
import { DetailInfoContainer } from './Detail';
import { Container, ModalContainer, CancelButton, CompleteButton, ButtonContainer } from './style';

interface PropType {
    status: string;
    taskList: TaskType[];
    hideModal: Function;
}

export const TaskModal: FC<PropType> = ({ status, taskList, hideModal }) => {
    const { basicInfoInput, detailInfoInput, handleCompleteButton } = useInputTask();

    return (
        <ModalContainer>
            <Container>
                <BasicInfoContainer taskList={taskList} basicInfoInput={basicInfoInput} />
                <DetailInfoContainer status={status} detailInfoInput={detailInfoInput} />
            </Container>
            <ButtonContainer>
                <CancelButton onClick={() => hideModal()}>취소</CancelButton>
                <CompleteButton onClick={handleCompleteButton}>완료</CompleteButton>
            </ButtonContainer>
        </ModalContainer>
    );
};
