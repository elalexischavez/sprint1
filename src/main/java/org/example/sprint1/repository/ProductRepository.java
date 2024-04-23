package org.example.sprint1.repository;

import org.example.sprint1.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository {
    private List<Product> products;
}
