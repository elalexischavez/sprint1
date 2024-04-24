package org.example.sprint1.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class BasicSellerDTO implements Serializable {
    @JsonProperty("user_id")
    private int sellerId;
    @JsonProperty("user_name")
    private String userName;
}
