import { restoreRedirectUrl } from './auth/after-redirect';

interface InitData {
  init: boolean;
}

export const initialize = (): InitData => {
  const search = new URLSearchParams(window.location.search);

  if (search.get('login') === 'true') {
    restoreRedirectUrl();
    return {
      init: false,
    };
  }

  return {
    init: true,
  };
}
