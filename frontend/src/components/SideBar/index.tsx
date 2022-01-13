import React from 'react';

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

interface project {
    name: string;
}

interface Props {
    props: Array<project> | Array<string>;
    delimiter: boolean;
}

export const SideBar = ({ props, delimiter }: Props) => (
    <>
        <VerticalBar />
        {delimiter ? (
            <ProjectContainer>
                <ProjectWrapper>
                    {props.map((el, idx) => (
                        <Project key={idx}>
                            <ProjectIcon />
                            <span>{(el as project).name}</span>
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
