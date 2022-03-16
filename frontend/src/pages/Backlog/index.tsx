import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';

import { getProject } from '../../apis/project';
import { getTasksGroupByStory } from '../../apis/task';
import { BacklogFeature } from '../../components/BacklogFeature';
import Header from '../../components/Header';
import Issue from '../../components/Issue';
import { TaskModal } from '../../components/Modal/Task';
import SideBar from '../../components/SideBar';
import useModal from '../../hooks/useModal';
import { projectState } from '../../stores/projectState';
import { StateType } from '../../types/project';
import { SimpleTaskType, StoryTaskType } from '../../types/task';
import { Container, TaskModalContainer, Wrapper } from './style';

interface TaskInfoType {
    taskId: number;
    status: string;
}

const Backlog = () => {
    const { state } = useLocation<StateType>();
    const [backlogTaskList, setBacklogTaskList] = useState<Array<StoryTaskType | SimpleTaskType>>([]);
    const { Modal, setModal } = useModal();
    const [taskInfo, setTaskInfo] = useState<TaskInfoType | null>(null);
    const [preTaskList, setPreTaskList] = useState<Array<SimpleTaskType>>([]);
    const setProject = useSetRecoilState(projectState);

    const showTaskModal = (event: React.MouseEventHandler, taskId: number, status: string) => {
        setTaskInfo({
            taskId,
            status,
        });
        setModal(event);
    };

    useEffect(() => {
        (async () => {
            const response = await getProject(state.projectId);
            setProject(response.data);

            const res = await getTasksGroupByStory(state.projectId);
            const storyTasks = res.data;

            setBacklogTaskList(storyTasks);
            setPreTaskList(() => {
                const preTasks: Array<SimpleTaskType> = [];
                storyTasks.map((storyTask) => storyTask.taskList).forEach((taskList) => preTasks.concat(taskList));
                return preTasks;
            });
        })();
    }, []);

    return (
        <>
            <Header />
            <SideBar />
            <BacklogFeature setBacklogTaskList={setBacklogTaskList} />
            <Container>
                <Wrapper>
                    {backlogTaskList.length > 0 && 'story' in backlogTaskList[0]
                        ? (backlogTaskList as Array<StoryTaskType>).map(({ story, taskList }: StoryTaskType, idx) => (
                              <div key={idx}>
                                  <Issue title={story} story />
                                  {taskList.map(
                                      ({ id, title, priority, managerAvatar, tags, status }: SimpleTaskType) => (
                                          <Issue
                                              key={id}
                                              id={id}
                                              title={title}
                                              priority={priority}
                                              manager={managerAvatar}
                                              tags={tags}
                                              status={status}
                                              showTaskModal={showTaskModal}
                                          />
                                      ),
                                  )}
                              </div>
                          ))
                        : (backlogTaskList as Array<SimpleTaskType>).map(
                              ({ id, title, priority, managerAvatar, tags, status }: SimpleTaskType) => (
                                  <Issue
                                      key={id}
                                      id={id}
                                      title={title}
                                      priority={priority}
                                      manager={managerAvatar}
                                      tags={tags}
                                      status={status}
                                      showTaskModal={showTaskModal}
                                  />
                              ),
                          )}
                </Wrapper>
            </Container>
            <TaskModalContainer>
                <Modal>
                    {taskInfo ? (
                        <TaskModal
                            taskId={taskInfo!.taskId}
                            status={taskInfo!.status}
                            taskList={preTaskList}
                            hideModal={setModal}
                            page="backlog"
                        />
                    ) : null}
                </Modal>
            </TaskModalContainer>
        </>
    );
};

export default Backlog;
