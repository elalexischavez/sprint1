package org.example.sprint1.service.follow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sprint1.dto.BasicCustomerDto;
import org.example.sprint1.dto.ExceptionDTO;
import org.example.sprint1.exception.BadRequestException;
import org.example.sprint1.repository.follow.IFollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.example.sprint1.entity.Customer;
import org.example.sprint1.entity.Seller;
import org.example.sprint1.exception.NotFoundException;
import org.example.sprint1.dto.SellerFollowerDto;
import org.example.sprint1.entity.Seller;
import org.example.sprint1.repository.CustomerRepository;
import org.example.sprint1.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import java.util.Comparator;
import java.util.stream.Stream;


@Service
public class FollowService implements IFollowService {
    @Autowired
    IFollowRepository followRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    SellerRepository sellerRepository;


    @Override
    public void userIdToFollow(int userId, int userIdToFollow) {
        //se optiene el resultado si existen id
        boolean respose =  followRepository.userIdToFollow(userId, userIdToFollow);

        if (respose) {
            throw new BadRequestException("id no encontrado");
        }
    }

    @Override
    public int countFollowers(int sellerId) {
        Seller seller = followRepository.countFollowers(sellerId);
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

    @Override
    public SellerFollowerDto getSellerFollowers(int userId, String order) {
        ObjectMapper mapper = new ObjectMapper();
        Seller seller =  sellerRepository.getSellerById(userId);

        Stream<BasicCustomerDto> customerStream = customerRepository.getCustomersThatFollowsSellersById(userId)
                .stream()
                .map(v -> mapper.convertValue(v, BasicCustomerDto.class));

        if ("name_asc".equals(order)) {
            customerStream = customerStream.sorted(Comparator.comparing(BasicCustomerDto::getUserName));
        } else if ("name_desc".equals(order)) {
            customerStream = customerStream.sorted(Comparator.comparing(BasicCustomerDto::getUserName).reversed());
        }
        return new SellerFollowerDto(userId, seller.getSellerName(), customerStream.toList());
    }
}
