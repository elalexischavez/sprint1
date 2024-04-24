package org.example.sprint1.repository;

import org.example.sprint1.entity.Post;

import java.util.List;
import java.util.Map;

public interface ISellerRepository {
    Map<Integer, List<Post>> findPostsByFollowing(List<Integer> sellers);
}
