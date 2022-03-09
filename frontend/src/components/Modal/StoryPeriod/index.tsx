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
    const [startAt, setStartAt] = useState<string | null>(story!.startAt ? story!.startAt : null);
    const [endAt, setEndAt] = useState<string | null>(story!.endAt ? story!.endAt : null);

    const changeStartAt = (e: React.ChangeEvent) => {
        setStartAt((e.target as HTMLInputElement).value);
    };

    const changeEndAt = (e: React.ChangeEvent) => {
        setEndAt((e.target as HTMLInputElement).value);
    };

    const updatePeriod = async () => {
        const formData = new FormData();
        formData.append('startAt', startAt!);
        formData.append('endAt', endAt!);

        await updateStoryPeriod(story!.id, formData);
        window.location.replace(`/roadmap`);
    };

    return (
        <Container>
            <Period>
                <span>스토리 기간 등록</span>
                <div>
                    <DatePicker type="date" value={startAt ? startAt : ''} onChange={changeStartAt} />
                    <span>부터</span>
                    <DatePicker type="date" value={endAt ? endAt : ''} onChange={changeEndAt} />
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
