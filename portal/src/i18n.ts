import i18n from "i18next";
import { initReactI18next } from "react-i18next";

const resources = {
  en: {
    translation: {
      WELCOME: 'Welcome',
      GO_TO_APP: 'Go to app',
      '404': '404 Not found',
      GO_TO_MAIN: 'Go to main',
      APP: 'App',
      LOGOUT: 'Logout',
      USERS: 'Users',
      ORDERS: 'Orders',
      GO_TO_HOME: 'Go to home',
      ID: 'Id',
      NAME: 'Name',
    }
  },
  br: {
    translation: {
      WELCOME: 'Receber',
      GO_TO_APP: 'Vá para o aplicativo',
      '404': '404 não encontrado',
      GO_TO_MAIN: 'Vá para o principal',
      APP: 'Aplicativo',
      LOGOUT: 'Sair',
      USERS: 'Comercial',
      ORDERS: 'Pedidos',
      GO_TO_HOME: 'Ir para casa',
      ID: 'Identidade',
      NAME: 'Nome',
    }
  }
};

export const setupTranslations = (lang: string) => {
  i18n
    .use(initReactI18next)
    .init({
      resources,
      lng: lang,
      interpolation: {
        escapeValue: false
      }
    });
}

