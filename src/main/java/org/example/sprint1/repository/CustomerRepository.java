package org.example.sprint1.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sprint1.entity.Customer;
import org.example.sprint1.entity.Seller;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomerRepository implements ICustomerRepository {
    private static List<Customer> customersList = new ArrayList<>();

    public CustomerRepository() throws IOException {
        loadCustomers();
    }

    private void loadCustomers() throws IOException {
        File file = ResourceUtils.getFile("classpath:customers.json");
        ObjectMapper objectMapper = new ObjectMapper();

        customersList = objectMapper.readValue(file, new TypeReference<List<Customer>>() {
        });
    }

    @Override
    public Customer findCustomerById(int id) {
        return customersList.stream()
                .filter(customer -> customer.getUserId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Customer> getCustomersList() {
        customersList.forEach(System.out::println);
        return customersList;
    }

    @Override
    public List<Customer> getCustomersThatFollowsSellersById(int id) {
        return  customersList.stream()
                .filter( v -> v.getSellers().contains(id))
                .toList();
    }
}
