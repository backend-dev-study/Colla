import React, { useState } from 'react';
import { useRecoilValue } from 'recoil';

import { createTaskStatus } from '../../../apis/project';
import { projectState } from '../../../stores/projectState';
import { ButtonContainer, Cancel, Input, Submit, Wrapper } from './style';

const TaskStatusModal = () => {
    const [input, setInput] = useState('');
    const project = useRecoilValue(projectState);

    const handleInput = (e: any) => setInput(e.target.value);

    const closeModal = () => {
        window.dispatchEvent(new Event('click'));
        setInput('');
    };

    const submitInput = async () => {
        await createTaskStatus(project.id, input);
        setInput('');
        window.location.replace('/kanban');
    };
    return (
        <Wrapper>
            <div>테스크 상태 추가</div>
            <Input placeholder="추가할 상태 이름을 입력하세요" value={input} onChange={handleInput} />
            <ButtonContainer>
                <Cancel onClick={closeModal}>취소</Cancel>
                <Submit onClick={submitInput}>완료</Submit>
            </ButtonContainer>
        </Wrapper>
    );
};

export default TaskStatusModal;
