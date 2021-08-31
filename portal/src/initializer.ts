import { restoreRedirectUrl } from './auth/after-redirect';


interface InitData {
  init: boolean;
  lang: string
}

interface AVAILABLE_LANGS {[key: string]: string};

const getAvailableLanguages = (): AVAILABLE_LANGS => {
  return SETTINGS.AVAILABLE_LANGUAGES.split(',').reduce((acc, curr) => {
    const a = curr.split(';');
    return {
      ...acc,
      [a[0]]: a[1]
    }
  }, {})
}

const getLangFromPath = () => window.location.pathname.match(/^\/(.*?)(\/|$)/)?.[1];
const getLangFromCookie = (availableLangs: AVAILABLE_LANGS) => {
  const locale =  document.cookie
    .split(';')
    .find((c) => c.trim().startsWith('Accept-Language'))
    ?.split('=')
    ?.[1];
  return Object.keys(availableLangs).find(key => availableLangs[key] === locale)
};

export const initialize = (): InitData => {
  const search = new URLSearchParams(window.location.search);
  if (search.get('login') === 'true') {
    restoreRedirectUrl();
    return {
      init: false,
      lang: ''
    };
  }

  const availableLangs = getAvailableLanguages();
  let lang = getLangFromPath();

   if (!lang || !Object.keys(availableLangs).includes(lang)) {
    lang = getLangFromCookie(availableLangs) || 'en';
    const rest = window.location.href.replace(window.location.origin, '');
    window.location.href = window.location.origin + '/' + lang + rest;
    return {
      init: false,
      lang
    };
  }

  document.cookie = `Accept-Language=${availableLangs[lang]};path=/`;
  return {
    init: true,
    lang,
  };
}
