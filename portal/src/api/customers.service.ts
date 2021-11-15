import { client } from './client';
import { Customer } from './customer';

export class CustomersService {
  public async getList(): Promise<Customer[]> {
    return client('/api/customers/customers');
  }
}

export const customersService = new CustomersService();
