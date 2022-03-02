import React, { FC, useEffect } from 'react';

import { getTask } from '../../../apis/task';
import useInputTask from '../../../hooks/useInputTask';
import { TaskType } from '../../../types/kanban';
import { SimpleTaskType } from '../../../types/task';
import { BasicInfoContainer } from './Basic';
import { CommentContainer } from './Comment';
import { DetailInfoContainer } from './Detail';
import { Container, ModalContainer, CancelButton, CompleteButton, ButtonContainer } from './style';

interface PropType {
    taskId: number | null;
    status: string;
    taskList: Array<TaskType | SimpleTaskType>;
    hideModal: Function;
}

export const TaskModal: FC<PropType> = ({ taskId, status, taskList, hideModal }) => {
    const { basicInfoInput, detailInfoInput, handleCompleteButton, setSelectedTask } = useInputTask();

    useEffect(() => {
        if (!taskId) {
            return;
        }

        (async () => {
            const res = await getTask(taskId);
            setSelectedTask(res.data);
        })();
    }, []);

    return (
        <ModalContainer>
            <Container>
                <BasicInfoContainer taskList={taskList} basicInfoInput={basicInfoInput} />
                <DetailInfoContainer status={status} detailInfoInput={detailInfoInput} />
            </Container>
            {taskId ? <CommentContainer taskId={taskId} /> : null}
            <ButtonContainer>
                <CancelButton onClick={() => hideModal()}>취소</CancelButton>
                <CompleteButton onClick={() => handleCompleteButton(taskId)}>{taskId ? '수정' : '완료'}</CompleteButton>
            </ButtonContainer>
        </ModalContainer>
    );
};
