import { AuthData } from './auth.context';

export const getAuth = async (): Promise<AuthData> => {
  if (!SETTINGS.AUTH) {
    return {
      user: {
        name: 'hello@example.com'
      },
      loginUrl: undefined,
      error: false,
    }
  }

  let whoami;
  try {
    whoami = await fetch('/whoami');
  } catch(e) {
    return {
      user: undefined,
      loginUrl: undefined,
      error: true,
    }
  }

  const status = whoami.status;
  if (status >= 500) {
    return {
      user: undefined,
      loginUrl: undefined,
      error: true,
    }
  }

  const body = (await whoami.text());
  if (status === 200) {
    return {
      user: JSON.parse(body),
      loginUrl: undefined,
      error: false,
    }
  }
  return {
    user: undefined,
    loginUrl: whoami.headers.get('X-auth-entrypoint') || '',
    error: false,
  }
}
