import React from 'react';
import { useHistory, useLocation } from 'react-router-dom';

import { useSetRecoilState } from 'recoil';
import EmptySrc from '../../../public/assets/images/empty.png';
import { projectState } from '../../stores/projectState';
import { ProjectType, StateType } from '../../types/project';
import {
    ProjectIcon,
    VerticalBar,
    ProjectContainer,
    MenuContainer,
    Project,
    Menu,
    ProjectWrapper,
    MenuWrapper,
} from './style';

interface Props {
    props?: Array<ProjectType>;
    project?: boolean;
}

const MENU = [
    { name: '칸반보드', path: '/kanban' },
    {
        name: '로드맵',
        path: '/roadmap',
    },
    { name: '백로그', path: '/backlog' },
    { name: '대시보드', path: '/dashboard' },
    { name: '모임장소', path: '/meeting-place' },
];

const SideBar = ({ props, project }: Props) => {
    const history = useHistory();
    const { state } = useLocation<StateType>();
    const setProjectState = useSetRecoilState(projectState);

    const enterProject = (project: ProjectType) => {
        const { id, name, description, thumbnail } = project;
        setProjectState({
            id,
            name,
            description,
            thumbnail,
            members: [],
        });
        history.push({
            pathname: '/kanban',
            state: { projectId: id },
        });
    };

    const handleClickSideBar = (path: string) => {
        history.push({
            pathname: path,
            state: { projectId: state.projectId },
        });
    };

    return (
        <>
            <VerticalBar />
            {project ? (
                <ProjectContainer>
                    <ProjectWrapper>
                        {props!.map((el, idx) => (
                            <Project key={idx} onClick={() => enterProject(el)}>
                                <ProjectIcon src={el.thumbnail ? el.thumbnail : EmptySrc} />
                                <span>{el.name}</span>
                            </Project>
                        ))}
                    </ProjectWrapper>
                </ProjectContainer>
            ) : (
                <MenuContainer>
                    <MenuWrapper>
                        {MENU.map(({ name, path }, idx) => (
                            <div key={idx} onClick={() => handleClickSideBar(path)}>
                                <Menu>{name}</Menu>
                            </div>
                        ))}
                    </MenuWrapper>
                </MenuContainer>
            )}
        </>
    );
};

export default SideBar;
