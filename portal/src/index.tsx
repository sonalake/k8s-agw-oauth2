import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import { setupTranslations } from './i18n';
import { initialize } from './initializer';
import { getAuth } from './auth/get-auth';
import { AuthContext } from './auth/auth.context';

declare global {
  const SETTINGS: {
    AUTH: boolean,
    AVAILABLE_LANGUAGES: string
  };
}

const {init, lang} = initialize();

if (init) {
  (async () => {
    setupTranslations(lang)
    const auth= await getAuth();

    ReactDOM.render(
      <React.StrictMode>
        <AuthContext.Provider value={auth}>
          <App lang={lang}/>
        </AuthContext.Provider>
      </React.StrictMode>,
      document.getElementById('root')
    );
  })();
}

