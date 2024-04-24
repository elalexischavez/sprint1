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
import java.util.NoSuchElementException;
import java.util.Optional;

@Repository
public class CustomerRepository {
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

    public List<Customer> getCustomersList() {
        customersList.forEach(System.out::println);
        return customersList;
    }

    public Customer getCustomerById(int id) {
        return customersList.stream()
                .filter(customer -> customer.getUserId() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No customer with ID  : " + id));
    }
}
