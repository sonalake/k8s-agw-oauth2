import React from 'react';
import { QueryClient, QueryClientProvider } from 'react-query';
import { BrowserRouter, Link, Route, Switch } from 'react-router-dom';
import './App.scss';
import { AppInternal } from './AppInternal';
import { Customers } from './conntainers/customers/Customers';
import { NotFound } from './conntainers/not-found/NotFoun';

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
                <Link to='/dashboard'>Go to app</Link>
              </div>
            </Route>
            <Route exact path={`/dashboard`}>
              <AppInternal>
                <h1>Welcome</h1>
              </AppInternal>
            </Route>
            <Route exact path={`/customers`}>
              <AppInternal>
                <Customers />
              </AppInternal>
            </Route>
            <Route>
              <NotFound />
            </Route>
          </Switch>
        </BrowserRouter>
      </QueryClientProvider>
    </div>
  );
}

export default App;
