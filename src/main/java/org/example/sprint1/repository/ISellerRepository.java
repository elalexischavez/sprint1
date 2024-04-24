package org.example.sprint1.repository;

import org.example.sprint1.entity.Post;
import org.example.sprint1.entity.Seller;

import java.util.List;

public interface ISellerRepository {
    List<Post> findPostsByFollowing(List<Integer> sellers);
    public Seller getSellerById(int id);
}
