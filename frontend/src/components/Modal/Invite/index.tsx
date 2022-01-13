import React, { ChangeEvent, useState } from 'react';

import { InviteButton, InviteEmailInput, InviteUser, Wrapper } from './style';

const members = [
    { name: 'asdf', email: 'asdfa@naver.com' },
    { name: 'were', email: 'zxczxc23@gmail.com' },
];

const InviteModal = () => {
    const [input, setInput] = useState('');

    const handleInputChange = (event: ChangeEvent<HTMLInputElement>) => setInput(event.target.value);

    return (
        <Wrapper>
            {members.map(({ name, email }) => (
                <div key={email}>
                    <div>{name}</div>
                    <div>{email}</div>
                </div>
            ))}
            <InviteUser>
                <InviteEmailInput placeholder="초대할 사용자 이메일" value={input} onChange={handleInputChange} />
                <InviteButton>초대</InviteButton>
            </InviteUser>
        </Wrapper>
    );
};

export default InviteModal;
