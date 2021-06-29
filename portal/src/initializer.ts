declare var SETTINGS: {AVAILABLE_LANGUAGES: string};

interface InitData {
  init: boolean;
  lang: string
}

export const initialize = (): InitData => {
  let lang = window.location.pathname.match(/^\/(.*?)(\/|$)/)?.[1] || '';
  const availableLangs: {[key: string]: string} = SETTINGS.AVAILABLE_LANGUAGES.split(',').reduce((acc, curr) => {
    const a = curr.split(';');
    return {
      ...acc,
      [a[0]]: a[1]
    }
  }, {})

  const search = new URLSearchParams(window.location.search);
  const logout = search.get('logout') === 'true';
  if (logout) {
    const logoutLang = search.get('lang') || '';
    window.location.href = window.location.origin + '/' + (Object.keys(availableLangs).includes(logoutLang) ? logoutLang : 'en');
    return {
      init: false,
      lang
    };
  } else if (!lang || !Object.keys(availableLangs).includes(lang)) {
    lang = 'en';
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
