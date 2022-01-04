import React from 'react';

import UserIcon from '../Icon/User';
import { Container, LeftNav, RightNav } from './style';

const userName = 'user';
const userProfileImg = undefined;
const userGithubId = 'user_github_id';

const Header = () => (
    <Container>
        <LeftNav></LeftNav>
        <RightNav>
            <UserIcon userName={userName} githubId={userGithubId} image={userProfileImg} size="small" />
        </RightNav>
    </Container>
);

export default Header;
