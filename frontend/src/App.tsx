import React from 'react';
import { Global } from '@emotion/react';

import Kanban from './pages/Kanban';
import reset from './styles/reset';

const App = () => (
    <div>
        <Global styles={reset} />
        <Kanban />
    </div>
);

export default App;
