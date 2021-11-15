import React from 'react';
import { useQuery } from 'react-query';
import { customersService } from '../../api/customers.service';

export function Customers() {

  const {data} = useQuery('customersList', () => customersService.getList());

  return (
    <>
      <h1>Customers</h1>
      <table>
        <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
        </tr>
        </thead>
        <tbody>
        {data && data.map((customer) =>
          <tr key={customer.id}>
            <td>{customer.id}</td>
            <td>{customer.name}</td>
          </tr>
        )}
        </tbody>
      </table>
    </>
  );
}
