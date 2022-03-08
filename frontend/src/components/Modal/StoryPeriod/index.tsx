import React, { FC } from 'react';

import { ButtonContainer, CancelButton, CompleteButton } from '../Task/style';
import { Container, DatePicker, Period } from './style';

interface PropType {
    showStoryPeriodModal: Function;
}

export const StoryPeriod: FC<PropType> = ({ showStoryPeriodModal }) => (
    <Container>
        <Period>
            <span>스토리 기간 등록</span>
            <div>
                <DatePicker type="date" />
                <span>부터</span>
                <DatePicker type="date" />
                <span>까지</span>
            </div>
        </Period>
        <ButtonContainer>
            <CancelButton onClick={() => showStoryPeriodModal()}>취소</CancelButton>
            <CompleteButton>완료</CompleteButton>
        </ButtonContainer>
    </Container>
);
