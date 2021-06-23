import React, { useEffect, useState } from 'react';

const getTimeLeft = () => {
  const sessionExpiresIn = (document.cookie || '')
    .split(';')
    .map((c) => c.trim())
    .find((c) => c.includes('SESSION_EXPIRES_IN'))
    ?.split('=')[1] || '';

  if (sessionExpiresIn === '') {
    return 0;
  }
  const now = Math.round(Date.now() / 1000);
  return parseInt(sessionExpiresIn) - now;
}

const logout = () => {
  const logoutForm = document.createElement('form');
  logoutForm.setAttribute('action', '/logout')
  logoutForm.setAttribute('method', 'POST')
  document.body.appendChild(logoutForm);
  logoutForm.submit();
}

export function Session() {

  const [timeLeft, setTimeLeft] = useState(10000);

  useEffect(() => {
    const interval = setInterval(() => {
      const timeLeft = getTimeLeft();
      if (timeLeft < 1) {
        logout()
      } else {
        setTimeLeft(timeLeft);
      }
    }, 1000);

    return () => clearInterval(interval);
  }, [])

  return (
    <>
      {timeLeft < 5 && <div className='session'>
        Your session expires in {timeLeft} seconds.
      </div>}
    </>
  );
}
