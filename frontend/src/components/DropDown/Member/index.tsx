import React, { FC, useEffect, useState } from 'react';

import { useRecoilValue } from 'recoil';
import { getProjectMembers } from '../../../apis/project';
import { projectState } from '../../../stores/projectState';
import { Avatar, Name } from '../../Task/style';
import { Member, MemberList } from './style';

interface PropType {
    setManager: Function;
    setMemberVisible: Function;
}

interface MemberType {
    id: number;
    name: string;
    avatar: string;
}

export const MemberDropDown: FC<PropType> = ({ setManager, setMemberVisible }) => {
    const project = useRecoilValue(projectState);
    const [memberList, setMemberList] = useState<MemberType[]>([]);

    const selectManager = (name: string) => {
        setManager(name);
        setMemberVisible();
    };

    useEffect(() => {
        (async () => {
            try {
                const res = await getProjectMembers(project.id);
                setMemberList(res.data);
            } catch (err) {
                setMemberList([]);
            }
        })();
    }, []);

    return (
        <MemberList>
            {memberList.map((member, idx) => (
                <Member key={idx} onClick={() => selectManager(member.name)}>
                    <Avatar src={member.avatar}></Avatar>
                    <Name>{member.name}</Name>
                </Member>
            ))}
        </MemberList>
    );
};
