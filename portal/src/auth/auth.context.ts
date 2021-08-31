import { createContext } from 'react';

interface AppUser {
  name: string;
}

export interface AuthData {
  user?: AppUser;
  loginUrl?: string;
  error?: boolean;
}

export const AuthContext = createContext<AuthData>({user: undefined, loginUrl: '', error: false});
