package org.example.sprint1.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private int idPost;
    private LocalDate date;
    private Product product;
    private int category;
    private double price;
    private boolean hasPromo;
    private double discount;
}
