import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import { initialize } from './initializer';
import { getAuth } from './auth/get-auth';
import { AuthContext } from './auth/auth.context';

declare global {
  const SETTINGS: {
    AUTH: boolean,
    AVAILABLE_LANGUAGES: string
  };
}

const {init} = initialize();

if (init) {
  (async () => {
    const auth= await getAuth();

    ReactDOM.render(
      <React.StrictMode>
        <AuthContext.Provider value={auth}>
          <App/>
        </AuthContext.Provider>
      </React.StrictMode>,
      document.getElementById('root')
    );
  })();
}

