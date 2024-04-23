package org.example.sprint1.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seller {
    @JsonProperty("seller_id")
    private int sellerId;
    @JsonProperty("seller_name")
    private String sellerName;
    private List<Post> posts;
    private List<Integer> customers;
}
