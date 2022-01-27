import React, { ChangeEvent, FC, useState } from 'react';

import { useRecoilValue } from 'recoil';
import { createStory } from '../../../apis/project';
import { projectState } from '../../../stores/projectState';
import { ButtonContainer, CancelButton, CompleteButton } from '../Task/style';
import { Container, Story, StoryArea } from './style';

interface PropType {
    showStoryModal: Function;
    selectStory: Function;
}

export const StoryModal: FC<PropType> = ({ showStoryModal, selectStory }) => {
    const [story, setStory] = useState<string>('');
    const project = useRecoilValue(projectState);

    const handleModifyStory = (event: ChangeEvent) => {
        setStory((event.target as HTMLTextAreaElement).value);
    };

    const handleCompleteButton = async () => {
        try {
            const res = await createStory(project.id, story);
            selectStory(res.data.title);
            showStoryModal();
        } catch (err) {
            showStoryModal();
        }
    };

    return (
        <Container>
            <Story>
                <span>스토리 등록하기</span>
                <StoryArea value={story} onChange={handleModifyStory} />
            </Story>
            <ButtonContainer>
                <CancelButton onClick={() => showStoryModal()}>취소</CancelButton>
                <CompleteButton onClick={handleCompleteButton}>완료</CompleteButton>
            </ButtonContainer>
        </Container>
    );
};
