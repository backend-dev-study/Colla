import React, { FC, useState } from 'react';

import DeleteIconSrc from '../../../../public/assets/images/delete.png';
import DownIconSrc from '../../../../public/assets/images/down.png';
import StarImgSrc from '../../../../public/assets/images/star.png';
import { TaskType } from '../../../types/kanban';
import { MemberDropDown } from '../../DropDown/Member';
import { PreTaskDropDown } from '../../DropDown/PreTask';
import { Task, TaskTitle } from '../../DropDown/PreTask/style';
import { StoryDropDown } from '../../DropDown/Story';
import { Star } from '../../Task/style';
import { StoryModal } from '../Story';
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
    ModalContainer,
    CancelButton,
    CompleteButton,
    ButtonContainer,
    PreTaskList,
    DeleteButton,
    PreTask,
} from './style';

interface PropType {
    status: string;
    taskList: TaskType[];
    hideModal: Function;
}

export const TaskModal: FC<PropType> = ({ status, taskList, hideModal }) => {
    const [story, setStory] = useState('');
    const [storyModalVisible, setStoryModalVisible] = useState(false);
    const [storyVisible, setStoryVisible] = useState(false);
    const [preTaskVisible, setPreTaskVisible] = useState(false);
    const [preTaskList, setPreTaskList] = useState([]);
    const [manager, setManager] = useState('');
    const [memberVisible, setMemberVisible] = useState(false);

    const showStoryModal = () => {
        setStoryModalVisible((prev) => !prev);
    };

    const showStoryList = () => {
        setStoryVisible((prev) => !prev);
    };

    const showPreTaskList = () => {
        setPreTaskVisible((prev) => !prev);
    };

    const deletePreTask = (idx: number) => {
        setPreTaskList((prev) => prev.filter((el) => el !== idx));
    };

    const showMemberList = () => {
        setMemberVisible((prev) => !prev);
    };

    return (
        <ModalContainer>
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
                        <DropDown onClick={showStoryList}>
                            {story}
                            <DownIcon src={DownIconSrc} />
                        </DropDown>
                        <AddButton onClick={showStoryModal}>추가하기</AddButton>
                    </TaskComponent>
                    <TaskComponent>
                        <span>선행 테스크</span>
                        <DropDown onClick={showPreTaskList}>
                            <DownIcon src={DownIconSrc} />
                        </DropDown>
                    </TaskComponent>
                    <PreTaskList>
                        {preTaskList.map((taskIdx, idx) => (
                            <PreTask key={idx}>
                                <Task>
                                    <TaskTitle>{taskList[taskIdx].title}</TaskTitle>
                                    <div>
                                        {Array(taskList[taskIdx].priority)
                                            .fill(0)
                                            .map((el, i) => i + 1)
                                            .map((el) => (
                                                <Star key={el} src={StarImgSrc} />
                                            ))}
                                    </div>
                                </Task>
                                <DeleteButton src={DeleteIconSrc} onClick={() => deletePreTask(taskIdx)} />
                            </PreTask>
                        ))}
                    </PreTaskList>
                </TaskContainer>
                <DetailContainer>
                    <DetailComponent>
                        담당자
                        <MemberList onClick={showMemberList}>
                            {manager}
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
                <CancelButton onClick={() => hideModal()}>취소</CancelButton>
                <CompleteButton>완료</CompleteButton>
            </ButtonContainer>
            {storyModalVisible ? <StoryModal showStoryModal={showStoryModal} selectStory={setStory} /> : null}
            {preTaskVisible ? (
                <PreTaskDropDown
                    taskList={taskList}
                    setPreTaskList={setPreTaskList}
                    setPreTaskVisible={setPreTaskVisible}
                />
            ) : null}
            {storyVisible ? <StoryDropDown setStory={setStory} setStoryVisible={setStoryVisible} /> : null}
            {memberVisible ? <MemberDropDown setManager={setManager} setMemberVisible={setMemberVisible} /> : null}
        </ModalContainer>
    );
};
