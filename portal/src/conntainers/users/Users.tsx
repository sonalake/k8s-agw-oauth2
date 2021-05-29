import React from 'react';
import { useQuery } from 'react-query';
import { usersService } from '../../api/users.service';

export function Users() {

  const {data} = useQuery('todos', () => usersService.getList());

  return (
    <>
      <h1>Users</h1>
      <table>
        <thead>
          <tr>
            <th>Id</th>
            <th>Name</th>
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
