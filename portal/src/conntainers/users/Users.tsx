import React from 'react';
import { useQuery } from 'react-query';
import { usersService } from '../../api/users.service';
import { Trans } from 'react-i18next';

export function Users() {

  const {data} = useQuery('todos', () => usersService.getList());

  return (
    <>
      <h1><Trans>USERS</Trans></h1>
      <table>
        <thead>
          <tr>
            <th><Trans>ID</Trans></th>
            <th><Trans>NAME</Trans></th>
          </tr>
        </thead>
        <tbody>
          {data && data.map((user) =>
            <tr key={user.id}>
              <td>{user.id}</td>
              <td>{user.name}</td>
            </tr>
          )}
        </tbody>
      </table>
    </>
  );
}
