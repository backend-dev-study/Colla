import React, { FC } from 'react';

import DownIconSrc from '../../../../public/assets/images/down.png';
import {
    TaskContainer,
    Title,
    TitleInput,
    TaskComponent,
    DescriptionArea,
    DropDown,
    AddButton,
    DownIcon,
    Container,
    DetailContainer,
    DetailComponent,
    MemberList,
    Status,
    Priority,
    Modal,
    CancelButton,
    CompleteButton,
    ButtonContainer,
} from './style';

interface PropType {
    status: string;
}

export const TaskModal: FC<PropType> = ({ status }) => {
    const addStory = async () => {};

    return (
        <Modal>
            <Container>
                <TaskContainer>
                    <Title>
                        <span>제목</span>
                        <TitleInput type="text" placeholder="제목을 입력하세요." />
                    </Title>
                    <TaskComponent>
                        <span>설명</span>
                        <DescriptionArea cols={43} rows={8} placeholder="설명을 입력하세요." />
                    </TaskComponent>
                    <TaskComponent>
                        <span>스토리</span>
                        <DropDown>
                            <DownIcon src={DownIconSrc} />
                        </DropDown>
                        <AddButton onClick={addStory}>추가하기</AddButton>
                    </TaskComponent>
                    <TaskComponent>
                        <span>선행 테스크</span>
                        <DropDown>
                            <DownIcon src={DownIconSrc} />
                        </DropDown>
                    </TaskComponent>
                </TaskContainer>
                <DetailContainer>
                    <DetailComponent>
                        담당자
                        <MemberList>
                            <DownIcon src={DownIconSrc} />
                        </MemberList>
                    </DetailComponent>
                    <DetailComponent>
                        상태
                        <Status>{status}</Status>
                    </DetailComponent>
                    <DetailComponent>
                        우선순위
                        <Priority>
                            {Array(5)
                                .fill(0)
                                .map((el, i) => i + 1)
                                .map((el, idx) => (
                                    <span key={idx}>{el}</span>
                                ))}
                        </Priority>
                    </DetailComponent>
                    <DetailComponent>태그</DetailComponent>
                </DetailContainer>
            </Container>
            <ButtonContainer>
                <CancelButton>취소</CancelButton>
                <CompleteButton>완료</CompleteButton>
            </ButtonContainer>
        </Modal>
    );
};
