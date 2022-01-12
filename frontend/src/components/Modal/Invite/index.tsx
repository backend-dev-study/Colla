import React from 'react';

import { InviteButton, InviteEmailInput, InviteUser, Wrapper } from './style';

const members = [
    { name: 'asdf', email: 'asdfa@naver.com' },
    { name: 'were', email: 'zxczxc23@gmail.com' },
];

const InviteModal = () => (
    <Wrapper>
        {members.map(({ name, email }) => (
            <div key={email}>
                <div>{name}</div>
                <div>{email}</div>
            </div>
        ))}
        <InviteUser>
            <InviteEmailInput />
            <InviteButton>초대</InviteButton>
        </InviteUser>
    </Wrapper>
);

export default InviteModal;
