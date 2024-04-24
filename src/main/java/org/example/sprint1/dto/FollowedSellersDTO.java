package org.example.sprint1.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowedSellersDTO implements Serializable {
    private int UserId;
    private String customerName;
    private List<BasicSellerDTO> followed;
}
