import React, { useEffect } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { ClipLoader } from 'react-spinners';

import { getAccessToken } from '../../apis/auth';
import { Container, Notice } from './style';

const LoginProcessing = ({ history, location }: RouteComponentProps) => {
    useEffect(() => {
        (async () => {
            try {
                const code = new URLSearchParams(location.search).get('code');
                if (!code) {
                    throw Error('github auth code does not exist');
                }

                const { accessToken } = await getAccessToken(code);
                accessToken ? history.push('/kanban') : history.push('/');
            } catch (error) {
                history.push('/');
            }
        })();
    }, []);

    return (
        <Container>
            <Notice>Loading...</Notice>
            <ClipLoader size="100" color="#fff"></ClipLoader>
        </Container>
    );
};

export default LoginProcessing;
