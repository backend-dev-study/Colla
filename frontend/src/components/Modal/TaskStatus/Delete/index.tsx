import React, { FC, useState } from 'react';
import { useRecoilValue } from 'recoil';

import { deleteTaskStatus } from '../../../../apis/project';
import { projectState } from '../../../../stores/projectState';
import { TaskStatusDropdown } from '../../../DropDown/TaskStatus';
import { Status, CurrentStatus, SelectedStatus, ButtonContainer, Cancel, Submit, Wrapper } from './style';

interface PropType {
    statuses: Array<string>;
    status: string;
}

const DeleteTaskStatusModal: FC<PropType> = ({ statuses, status }) => {
    const [selected, setSelected] = useState(statuses[0]);
    const [visible, setVisible] = useState(false);
    const project = useRecoilValue(projectState);

    const toggleVisible = () => setVisible(!visible);

    const closeModal = () => {
        window.dispatchEvent(new Event('click'));
    };

    const submitInput = async () => {
        await deleteTaskStatus(project.id, status, selected);
        window.location.replace('/kanban');
    };
    return (
        <Wrapper>
            <Status>
                <div>변경 전 상태</div>
                <CurrentStatus>{status}</CurrentStatus>
            </Status>
            <Status>
                <div>변경 후 상태</div>
                <SelectedStatus onClick={toggleVisible}>
                    {selected}{' '}
                    {visible ? (
                        <TaskStatusDropdown
                            taskStatuses={statuses}
                            setSelected={setSelected}
                            setVisible={toggleVisible}
                        />
                    ) : null}
                </SelectedStatus>
            </Status>

            <ButtonContainer>
                <Cancel onClick={closeModal}>취소</Cancel>
                <Submit onClick={submitInput}>완료</Submit>
            </ButtonContainer>
        </Wrapper>
    );
};

export default DeleteTaskStatusModal;
