package org.example.sprint1.repository;

import org.example.sprint1.entity.Seller;

import java.util.Optional;

public interface ISellerRepository {
    public Seller getSellerById(int id);
}
