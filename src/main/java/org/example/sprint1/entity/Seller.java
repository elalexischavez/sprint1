package org.example.sprint1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seller {
    private int userId;
    private String userName;
    private List<Post> posts;
}
