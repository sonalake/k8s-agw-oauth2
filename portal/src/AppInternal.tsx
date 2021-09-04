import React, { useContext } from 'react';
import './App.scss';
import { Link, NavLink, Route, Switch, useRouteMatch } from 'react-router-dom';
import { Users } from './conntainers/users/Users';
import { Session } from './components/session/Session';
import { Trans } from 'react-i18next';
import { AuthContext } from './auth/auth.context';
import { setRedirectUrl } from './auth/after-redirect';

let didLogIn = false;

export function AppInternal() {
  let { path } = useRouteMatch();
  const {user, loginUrl, error} = useContext(AuthContext);

  if (error && SETTINGS.AUTH) {
    return <>'Could not fetch data. Please try again later.'</>
  }
  //We are causing side effect in render phase! That is acceptable, because if the condition that triggers that effect is true,
  //we will leave the application.
  if (didLogIn) {
    return null;
  }
  if (!user && SETTINGS.AUTH && !didLogIn) {
    setRedirectUrl();
    window.location.href = loginUrl!;
    didLogIn = true;
    return null;
  }

  return (
    <>
      {SETTINGS.AUTH && <Session/>}
      <header>
        <Link to="/app" className='brand'><Trans>APP</Trans></Link>
        <p className='user-name'>{user?.name || 'NO_NAME'}</p>
        <form action="/logout" method="POST">
          <button type='submit'><Trans>LOGOUT</Trans></button>
        </form>
      </header>
      <aside>
        <ul>
          <li>
            <NavLink to={`${path}/users`} activeClassName='active'><Trans>USERS</Trans></NavLink>
          </li>
          <li>
            <NavLink to={`${path}/orders`} activeClassName='active'><Trans>ORDERS</Trans></NavLink>
          </li>
        </ul>
      </aside>
      <main>
        <Switch>
          <Route exact path={`${path}/`}>
            <h1><Trans>WELCOME</Trans></h1>
          </Route>
          <Route exact path={`${path}/users`}>
            <Users/>
          </Route>
          <Route exact path={`${path}/orders`}>
            <h1><Trans>ORDERS</Trans></h1>
          </Route>
          <Route>
            <h1><Trans>404</Trans></h1>
            <Link to='/app'><Trans>GO_TO_HOME</Trans></Link>
          </Route>
        </Switch>
      </main>
    </>
  );
}
