package org.example.sprint1.service.follow;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sprint1.dto.BasicCustomerDto;
import org.example.sprint1.dto.ExceptionDTO;
import org.example.sprint1.dto.SellerFollowerDto;
import org.example.sprint1.entity.Seller;
import org.example.sprint1.repository.CustomerRepository;
import org.example.sprint1.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.stream.Stream;


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
