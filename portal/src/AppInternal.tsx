import React from 'react';
import './App.scss';
import { Link, NavLink, Route, Switch, useRouteMatch } from 'react-router-dom';
import { Users } from './conntainers/users/Users';
import { Session } from './components/session/Session';

export function AppInternal() {
  let { path } = useRouteMatch();

  return (
    <>
      <Session/>
      <header>
        <Link to="/app">App</Link>
        <form action="/logout" method="POST">
          <button type='submit'>Logout</button>
        </form>
      </header>
      <aside>
        <ul>
          <li>
            <NavLink to={`${path}/users`} activeClassName='active'>Users</NavLink>
          </li>
          <li>
            <NavLink to={`${path}/orders`} activeClassName='active'>Orders</NavLink>
          </li>
        </ul>
      </aside>
      <main>
        <Switch>
          <Route exact path={`${path}/`}>
            <h1>Welcome</h1>
          </Route>
          <Route exact path={`${path}/users`}>
            <Users/>
          </Route>
          <Route exact path={`${path}/orders`}>
            <h1>Orders</h1>
          </Route>
          <Route>
            <h1>404 Not found</h1>
            <Link to='/app'>Go to home</Link>
          </Route>
        </Switch>
      </main>
    </>
  );
}
