const AFTER_REDIRECT_KEY = 'AFTER_REDIRECT';

export const setRedirectUrl = () => {
  localStorage.setItem(AFTER_REDIRECT_KEY, window.location.href);
}

export const restoreRedirectUrl = () => {
  const redirectUrl = localStorage.getItem(AFTER_REDIRECT_KEY) || '/';
  localStorage.removeItem(AFTER_REDIRECT_KEY);
  window.location.href = redirectUrl;
}
