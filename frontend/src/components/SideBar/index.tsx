import React from 'react';

import EmptySrc from '../../../public/assets/images/empty.png';
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

interface Project {
    id: number;
    name: string;
    thumbnail: string;
}

interface Props {
    props: Array<Project | string>;
    project?: boolean;
}

export const SideBar = ({ props, project }: Props) => (
    <>
        <VerticalBar />
        {project ? (
            <ProjectContainer>
                <ProjectWrapper>
                    {props.map((el, idx) => (
                        <Project key={idx}>
                            <ProjectIcon src={(el as Project).thumbnail ? (el as Project).thumbnail : EmptySrc} />
                            <span>{(el as Project).name}</span>
                        </Project>
                    ))}
                </ProjectWrapper>
            </ProjectContainer>
        ) : (
            <MenuContainer>
                <MenuWrapper>
                    {props.map((el) => (
                        <>
                            <Menu>{el}</Menu>
                        </>
                    ))}
                </MenuWrapper>
            </MenuContainer>
        )}
    </>
);
