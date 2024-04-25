package org.example.sprint1.service.follow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sprint1.dto.BasicSellerDTO;
import org.example.sprint1.dto.ExceptionDTO;
import org.example.sprint1.dto.FollowedSellersDTO;
import org.example.sprint1.entity.Customer;
import org.example.sprint1.repository.CustomerRepository;
import org.example.sprint1.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


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
    public FollowedSellersDTO getFollowedSellers(int userId, String order) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        Customer customer  =  customerRepository.getCustomerById(userId);
        List<BasicSellerDTO> sellers = sellerRepository.getCustomersThatFollowsSellersById(userId)
                .stream().map( v -> mapper.convertValue(v, BasicSellerDTO.class)).collect(Collectors.toList());

        if ("name_asc".equals(order)) {
            sellers.sort(Comparator.comparing(BasicSellerDTO::getSellerName));
        } else if ("name_desc".equals(order)) {
            sellers.sort(Comparator.comparing(BasicSellerDTO::getSellerName).reversed());
        }

        return new FollowedSellersDTO(userId, customer.getUserName(), sellers);
    }
}
