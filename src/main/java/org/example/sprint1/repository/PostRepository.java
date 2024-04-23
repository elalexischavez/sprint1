package org.example.sprint1.repository;

import org.example.sprint1.entity.Post;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepository {
    private List<Post> posts;
}
