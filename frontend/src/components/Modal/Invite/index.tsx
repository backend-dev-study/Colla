import React, { ChangeEvent, useState } from 'react';

import { useRecoilValue } from 'recoil';
import { inviteUser } from '../../../apis/project';
import { projectState } from '../../../stores/projectState';
import {
    ImageContainer,
    InviteButton,
    InviteEmailInput,
    InviteUser,
    Member,
    MemberImage,
    MemberInfo,
    MemberList,
    Wrapper,
} from './style';

const InviteModal = () => {
    const project = useRecoilValue(projectState);
    const [input, setInput] = useState('');

    const handleInputChange = (event: ChangeEvent<HTMLInputElement>) => setInput(event.target.value);

    return (
        <Wrapper>
            <MemberList>
                {project.members.map(({ displayName, githubId, avatar }) => (
                    <Member key={githubId}>
                        <MemberImage>
                            <ImageContainer image={avatar} />
                        </MemberImage>
                        <MemberInfo>
                            <div>{displayName}</div>
                            <div>{githubId}</div>
                        </MemberInfo>
                    </Member>
                ))}
            </MemberList>
            <InviteUser>
                <InviteEmailInput
                    placeholder="초대할 사용자 깃허브 아이디"
                    value={input}
                    onChange={handleInputChange}
                />
                <InviteButton onClick={() => inviteUser(project.id, input)}>초대</InviteButton>
            </InviteUser>
        </Wrapper>
    );
};

export default InviteModal;
