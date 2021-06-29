import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import { setupTranslations } from './i18n';
import { initialize } from './initializer';

const {init, lang} = initialize();

if (init) {
  setupTranslations(lang);
  ReactDOM.render(
    <React.StrictMode>
      <App lang={lang}/>
    </React.StrictMode>,
    document.getElementById('root')
  );
}

