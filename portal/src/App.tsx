import React from 'react';
import './App.scss';
import { BrowserRouter, Link, Route, Switch } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query';
import { AppInternal } from './AppInternal';
import { Trans } from 'react-i18next';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: false,
      refetchOnWindowFocus: false,
    }
  }
})
interface Props {
  lang: string;
}

function App({lang}: Props) {
  return (
    <div className='app'>
        <QueryClientProvider client={queryClient}>
          <BrowserRouter basename={lang}>
            <Switch>
              <Route exact path="/">
                <div className='landing-page'>
                  <h1><Trans>WELCOME</Trans></h1>
                  <a href={`/${lang}/app`}><Trans>GO_TO_APP</Trans></a>
                </div>
              </Route>
              <Route path="/app">
                <AppInternal/>
              </Route>
              <Route>
                <div className="landing-page">
                  <h1><Trans>404</Trans></h1>
                  <Link to='/'><Trans>GO_TO_MAIN</Trans></Link>
                </div>
              </Route>
            </Switch>
          </BrowserRouter>
        </QueryClientProvider>
    </div>
  );
}

export default App;
