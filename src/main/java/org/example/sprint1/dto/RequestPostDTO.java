package org.example.sprint1.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.sprint1.entity.Product;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestPostDTO {
   @JsonProperty("user_id")
    private int userId;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
    private Product product;
    private int category;
    private double price;
}
