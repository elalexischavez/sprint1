package org.example.sprint1.service.follow;

import org.example.sprint1.dto.ExceptionDTO;
import org.example.sprint1.entity.Customer;
import org.example.sprint1.entity.Seller;
import org.example.sprint1.exception.NotFoundException;
import org.example.sprint1.repository.follow.IFollowRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class FollowService implements IFollowService {
    @Autowired
    IFollowRespository followRespository;
    @Override
    public ExceptionDTO userIdToFollow(int userId, int userIdToFollow) {
        return null;
    }

    @Override
    public int countFollowers(int sellerId) {
        Seller seller = followRespository.countFollowers(sellerId);
        if(seller == null){
            throw new NotFoundException("Vendedor no encontrado");
        }
        return seller.getFollowers().size();
    }

    @Override
    public ExceptionDTO unfollowSeller(int userId, int userIdToFollow) {
        List<Seller> sellers = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();
        Customer customer = customers.stream()
                    .filter(c -> c.getUserId() == userId)
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
        Seller seller = sellers.stream()
                    .filter(s -> s.getSellerId() == userIdToFollow)
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Vendedor no encontrado"));
        // Remover el sellerId de la lista de vendedores que el usuario está siguiendo
        customer.getSellers().removeIf(sellerIdFollowed -> sellerIdFollowed == userIdToFollow);
        // Remover el userId de la lista de seguidores del vendedor
        seller.getFollowers().removeIf(followerId -> followerId == userId);
        return null;
    }
}
