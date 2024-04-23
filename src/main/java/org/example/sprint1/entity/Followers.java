package org.example.sprint1.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Followers {
    private int userId;
    private int userIdToFollow;
}
