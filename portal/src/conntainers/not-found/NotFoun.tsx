import React from 'react';
import { Link } from 'react-router-dom';

export function NotFound() {
  return (
    <div className="landing-page">
      <h1>Page not found</h1>
      <Link to='/'>Go to main page</Link>
    </div>
  );
}
