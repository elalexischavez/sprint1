package org.example.sprint1.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("user_name")
    private String userName;
}
