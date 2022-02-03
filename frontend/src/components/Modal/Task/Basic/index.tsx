import React, { FC, useState } from 'react';

import DownIconSrc from '../../../../../public/assets/images/down.png';
import { TaskType } from '../../../../types/kanban';
import { BasicInputType } from '../../../../types/task';
import { PreTaskDropDown } from '../../../DropDown/PreTask';
import { StoryDropDown } from '../../../DropDown/Story';
import { PreTaskList } from '../../../List/PreTaskList';
import { StoryModal } from '../../Story';
import { DownIcon } from '../style';
import { AddButton, DescriptionArea, DropDown, TaskComponent, TaskContainer, Title, TitleInput } from './style';

interface PropType {
    taskList: Array<TaskType>;
    basicInfoInput: BasicInputType;
}

export const BasicInfoContainer: FC<PropType> = ({ taskList, basicInfoInput }) => {
    const {
        taskInput,
        handleChangeTitle,
        handleChangeDescription,
        handleChangeStory,
        handleAddPreTask,
        handleDeletePreTask,
    } = basicInfoInput;
    const { title, description, story, preTasks } = taskInput;
    const [storyVisible, setStoryVisible] = useState(false);
    const [storyModalVisible, setStoryModalVisible] = useState(false);
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

    return (
        <>
            <TaskContainer>
                <Title>
                    <span>제목</span>
                    <TitleInput
                        type="text"
                        placeholder="제목을 입력하세요."
                        value={title}
                        onChange={(e) => handleChangeTitle(e)}
                    />
                </Title>
                <TaskComponent>
                    <span>설명</span>
                    <DescriptionArea
                        cols={43}
                        rows={8}
                        placeholder="설명을 입력하세요."
                        value={description}
                        onChange={(e) => handleChangeDescription(e)}
                    />
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
                <PreTaskList preTaskList={preTasks} taskList={taskList} handleDeletePreTask={handleDeletePreTask} />
            </TaskContainer>
            {storyModalVisible ? <StoryModal showStoryModal={showStoryModal} selectStory={handleChangeStory} /> : null}
            {storyVisible ? <StoryDropDown setStory={handleChangeStory} setStoryVisible={setStoryVisible} /> : null}
            {preTaskVisible ? (
                <PreTaskDropDown
                    taskList={taskList}
                    handleChangePreTasks={handleAddPreTask}
                    setPreTaskVisible={setPreTaskVisible}
                />
            ) : null}
        </>
    );
};
