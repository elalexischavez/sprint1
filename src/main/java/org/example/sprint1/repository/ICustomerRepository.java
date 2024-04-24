package org.example.sprint1.repository;

import org.example.sprint1.entity.Customer;

public interface ICustomerRepository {
    Customer findCustomerById(int id);
}
