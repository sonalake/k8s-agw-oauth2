import React, { PropsWithChildren, useContext } from 'react';
import { Link, NavLink } from 'react-router-dom';
import './App.scss';
import { setRedirectUrl } from './auth/after-redirect';
import { AuthContext } from './auth/auth.context';
import { Session } from './components/session/Session';

let didLogIn = false;

export function AppInternal({children}: PropsWithChildren<unknown>) {
  const {user, loginUrl, error} = useContext(AuthContext);

  if (error && SETTINGS.AUTH) {
    return <>'Could not fetch data. Please try again later.'</>
  }

  if (didLogIn) {
    return null;
  }

  if (!user && SETTINGS.AUTH && !didLogIn) {
    setRedirectUrl();
    //We are causing side effect in render phase! That is acceptable, because if the condition that triggers that effect is true,
    //we will leave the application.
    window.location.href = loginUrl!;
    didLogIn = true;
    return null;
  }

  return (
    <>
      {SETTINGS.AUTH && <Session />}
      <header>
        <Link to="/dashboard" className='brand'>App</Link>
        <p className='user-name'>{user?.name || 'NO_NAME'}</p>
        <form action="/logout" method="POST">
          <button type='submit'>Logout</button>
        </form>
      </header>
      <aside>
        <ul>
          <li>
            <NavLink to={`/customers`}
                     activeClassName='active'>Customers</NavLink>
          </li>
        </ul>
      </aside>
      <main>
        {children}
      </main>
    </>
  );
}
