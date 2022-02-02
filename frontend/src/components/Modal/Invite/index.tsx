import React, { ChangeEvent, useState } from 'react';

import { useRecoilValue } from 'recoil';
import { projectMemberState } from '../../../stores/projectState';
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
    const members = useRecoilValue(projectMemberState);
    const [input, setInput] = useState('');

    const handleInputChange = (event: ChangeEvent<HTMLInputElement>) => setInput(event.target.value);

    return (
        <Wrapper>
            <MemberList>
                {members.map(({ displayName, githubId, avatar }) => (
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
                <InviteEmailInput placeholder="초대할 사용자 이메일" value={input} onChange={handleInputChange} />
                <InviteButton>초대</InviteButton>
            </InviteUser>
        </Wrapper>
    );
};

export default InviteModal;
