package org.example.sprint1.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.sprint1.entity.Post;
import org.example.sprint1.entity.Seller;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Data
@Repository
public class SellerRepository implements ISellerRepository {
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

    public boolean postIdExist(int id){
        return sellersList.stream().anyMatch(seller -> seller.getPosts()
                .stream().anyMatch(post -> post.getPostId() == id));
    }

    @Override
    public List<Post> findPostsByFollowing(List<Integer> sellers) {
        List<Seller> sellersMatch = new ArrayList<>();
        List<Post> postsMatch = new ArrayList<>();

        // Obtenemos cada seller que el customer sigue
        for (Integer sellerId : sellers) {
            sellersMatch.add(filterSellerById(sellerId));
        }

        // Agregamos a una lista todos los post que cumplen con las especificaciones
        for(Seller seller : sellersMatch) {
            postsMatch.addAll(findPostsWithTwoWeeksOld(seller.getPosts()));
        }

        // Ordenar la lista de posts por fecha de manera descendente
        postsMatch.sort(Comparator.comparing(Post::getDate).reversed());

        return postsMatch;
    }

    private List<Post> findPostsWithTwoWeeksOld(List<Post> posts) {
        // Obtener la fecha de hace dos semanas y la actual
        LocalDate twoWeeksBefore = LocalDate.now().minusWeeks(2);
        LocalDate currentDate = LocalDate.now();

        // Retorna todos los post del seller que hayan sido publicados en las
        // últimas dos semanas
        return posts.stream()
                .filter(post ->
                    (post.getDate().isAfter(twoWeeksBefore) ||
                    post.getDate().isEqual(twoWeeksBefore)) &&
                    (post.getDate().isBefore(currentDate) ||
                    post.getDate().isEqual(currentDate))
                )
                .toList();
    }
}
