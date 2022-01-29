import React, { FC, useState } from 'react';

import DownIconSrc from '../../../../../public/assets/images/down.png';
import { TaskType } from '../../../../types/kanban';
import { PreTaskDropDown } from '../../../DropDown/PreTask';
import { StoryDropDown } from '../../../DropDown/Story';
import { PreTaskList } from '../../../List/PreTaskList';
import { StoryModal } from '../../Story';
import { DownIcon } from '../style';
import { AddButton, DescriptionArea, DropDown, TaskComponent, TaskContainer, Title, TitleInput } from './style';

interface PropType {
    taskList: Array<TaskType>;
}

export const BasicInfoContainer: FC<PropType> = ({ taskList }) => {
    const [story, setStory] = useState('');
    const [storyVisible, setStoryVisible] = useState(false);
    const [storyModalVisible, setStoryModalVisible] = useState(false);
    const [preTaskList, setPreTaskList] = useState([]);
    const [preTaskVisible, setPreTaskVisible] = useState(false);

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

    return (
        <>
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
                <PreTaskList preTaskList={preTaskList} taskList={taskList} deletePreTask={deletePreTask} />
            </TaskContainer>
            {storyModalVisible ? <StoryModal showStoryModal={showStoryModal} selectStory={setStory} /> : null}
            {storyVisible ? <StoryDropDown setStory={setStory} setStoryVisible={setStoryVisible} /> : null}
            {preTaskVisible ? (
                <PreTaskDropDown
                    taskList={taskList}
                    setPreTaskList={setPreTaskList}
                    setPreTaskVisible={setPreTaskVisible}
                />
            ) : null}
        </>
    );
};
