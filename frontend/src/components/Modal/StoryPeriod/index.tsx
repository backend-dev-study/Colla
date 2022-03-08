import React, { FC, useState } from 'react';

import { updateStoryPeriod } from '../../../apis/story';
import { StoryType } from '../../../types/roadmap';
import { ButtonContainer, CancelButton, CompleteButton } from '../Task/style';
import { Container, DatePicker, Period } from './style';

interface PropType {
    story: StoryType | null;
    showStoryPeriodModal: Function;
}

export const StoryPeriod: FC<PropType> = ({ story, showStoryPeriodModal }) => {
    const [startAt, setStartAt] = useState<string>(story!.startAt ? story!.startAt : '');
    const [endAt, setEndAt] = useState<string>(story!.endAt ? story!.endAt : '');

    const changeStartAt = (e: React.ChangeEvent) => {
        setStartAt((e.target as HTMLInputElement).value);
    };

    const changeEndAt = (e: React.ChangeEvent) => {
        setEndAt((e.target as HTMLInputElement).value);
    };

    const updatePeriod = async () => {
        const formData = new FormData();
        formData.append('startAt', startAt);
        formData.append('endAt', endAt);

        await updateStoryPeriod(story!.id, formData);
    };

    return (
        <Container>
            <Period>
                <span>스토리 기간 등록</span>
                <div>
                    <DatePicker type="date" value={startAt} onChange={changeStartAt} />
                    <span>부터</span>
                    <DatePicker type="date" value={endAt} onChange={changeEndAt} />
                    <span>까지</span>
                </div>
            </Period>
            <ButtonContainer>
                <CancelButton onClick={() => showStoryPeriodModal()}>취소</CancelButton>
                <CompleteButton onClick={updatePeriod}>완료</CompleteButton>
            </ButtonContainer>
        </Container>
    );
};
