package org.example.sprint1.service.follow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sprint1.dto.BasicSellerDTO;
import org.example.sprint1.dto.ExceptionDTO;
import org.example.sprint1.dto.FollowedSellersDTO;
import org.example.sprint1.entity.Customer;
import org.example.sprint1.entity.Seller;
import org.example.sprint1.repository.CustomerRepository;
import org.example.sprint1.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FollowService implements IFollowService {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    SellerRepository sellerRepository;

    @Override
    public ExceptionDTO userIdToFollow(int userId, int userIdToFollow) {
        return null;
    }
    @Override
    public FollowedSellersDTO getFollowedSellers(int userId){
        ObjectMapper mapper = new ObjectMapper();
        Customer customer  =  customerRepository.getCustomerById(userId);
        return new FollowedSellersDTO(userId, customer.getUserName(),
                sellerRepository.getCustomersThatFollowsSellersById(userId)
                        .stream().map( v -> mapper.convertValue(v, BasicSellerDTO.class)).toList()
        );


    }

}
