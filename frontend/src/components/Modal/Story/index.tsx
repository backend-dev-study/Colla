import React, { FC, MouseEventHandler } from 'react';

import { ButtonContainer, CancelButton, CompleteButton } from '../Task/style';
import { Container, Story, StoryArea } from './style';

interface PropType {
    showStoryModal: MouseEventHandler;
}

export const StoryModal: FC<PropType> = ({ showStoryModal }) => (
    <Container>
        <Story>
            <span>스토리 등록하기</span>
            <StoryArea />
        </Story>
        <ButtonContainer>
            <CancelButton onClick={showStoryModal}>취소</CancelButton>
            <CompleteButton>완료</CompleteButton>
        </ButtonContainer>
    </Container>
);
