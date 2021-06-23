import React from 'react';
import './App.scss';
import { BrowserRouter, Link, Route, Switch } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query';
import { AppInternal } from './AppInternal';
import { Session } from './components/session/Session';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: false,
      refetchOnWindowFocus: false,
    }
  }
})

function App() {
  return (
    <div className='app'>
      <QueryClientProvider client={queryClient}>
        <BrowserRouter>
          <Switch>
            <Route exact path="/">
              <div className='landing-page'>
                <h1>Welcome</h1>
                <a href="app">Go to app</a>
              </div>
            </Route>
            <Route path="/app">
              <AppInternal/>
            </Route>
            <Route>
              <div className="landing-page">
                <h1>404 Not found</h1>
                <Link to='/'>Go to main</Link>
              </div>
            </Route>
          </Switch>
        </BrowserRouter>
      </QueryClientProvider>
    </div>
  );
}

export default App;
