package org.example.sprint1.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.sprint1.entity.Seller;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Data
@Repository
public class SellerRepository {
    private List<Seller> sellersList = new ArrayList<>();

    public SellerRepository() throws IOException {
        loadSellers();
    }

    private void loadSellers() throws IOException {
        File file = ResourceUtils.getFile("classpath:sellers.json");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        sellersList.addAll(objectMapper.readValue(file, new TypeReference<List<Seller>>() {
        }));
    }

    public Seller filterSellerById(int id){
        return sellersList.stream().filter(seller -> seller.getSellerId() == id)
                .findFirst()
                .orElse(null);
    }

    public boolean productIdExists(int id) {
        return sellersList.stream()
                .anyMatch(seller -> seller.productIdExists(id));
    }
}
