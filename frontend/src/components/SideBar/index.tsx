import React from 'react';

import { Link, useHistory } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import EmptySrc from '../../../public/assets/images/empty.png';
import { projectState } from '../../stores/projectState';
import { ProjectType } from '../../types/project';
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
    { kor: '칸반보드', en: 'kanban' },
    {
        kor: '로드맵',
        en: 'roadmap',
    },
    { kor: '백로그', en: 'backlog' },
    { kor: '대시보드', en: 'dashboard' },
    { kor: '지도', en: 'map' },
];

export const SideBar = ({ props, project }: Props) => {
    const history = useHistory();
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
                        {MENU.map(({ kor, en }, idx) => (
                            <div key={idx}>
                                <Link to={`/${en}`}>
                                    <Menu>{kor}</Menu>
                                </Link>
                            </div>
                        ))}
                    </MenuWrapper>
                </MenuContainer>
            )}
        </>
    );
};
