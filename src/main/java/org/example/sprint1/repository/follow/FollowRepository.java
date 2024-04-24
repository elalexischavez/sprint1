package org.example.sprint1.repository.follow;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sprint1.entity.Customer;
import org.example.sprint1.entity.Seller;
import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class FollowRepository implements IFollowRepository{
    private static List<Customer> customersList = new ArrayList<>();
    private static final List<Seller> sellersList = new ArrayList<>();

    public FollowRepository() throws IOException {
        loadCustomers();
        loadSellers();
    }

    private void loadCustomers() throws IOException {
        File file = ResourceUtils.getFile("classpath:customers.json");
        ObjectMapper objectMapper = new ObjectMapper();

        customersList = objectMapper.readValue(file, new TypeReference<List<Customer>>() {
        });
    }

    private void loadSellers() throws IOException {
        File file = ResourceUtils.getFile("classpath:sellers.json");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        sellersList.addAll(objectMapper.readValue(file, new TypeReference<List<Seller>>() {
        }));
    }


    @Override
    public boolean userIdToFollow(int userId, int userIdToFollow) {

        // verifica que el customers excita
        Customer customer = customersList.stream()
                .filter(value -> value.getUserId() == userId).findAny().orElse(null);

        // verifica que el sellers excita
        Seller seller = sellersList
                .stream().filter(value -> value.getSellerId() == userIdToFollow).findAny().orElse(null);

        if(customer == null || seller == null ) return true;


        //se optiene la lista para agregar el foller
        List<Integer> followCustomers =customer.getSellers();
        followCustomers.add(userIdToFollow);

        //se optiene la lista para agregar el follwings
        List<Integer> followSeller = seller.getFollowers();
        followSeller.add(userId);

        // se agrega lista actualizada
        customer.setSellers(followCustomers);
        seller.setFollowers(followSeller);

        return false;
    }
}
