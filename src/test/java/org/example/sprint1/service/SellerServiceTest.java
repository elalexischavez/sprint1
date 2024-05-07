package org.example.sprint1.service;

import org.example.sprint1.dto.PostDTO;
import org.example.sprint1.dto.ResponsePostDTO;
import org.example.sprint1.entity.Customer;
import org.example.sprint1.entity.Post;
import org.example.sprint1.repository.ICustomerRepository;
import org.example.sprint1.repository.ISellerRepository;
import org.example.sprint1.service.seller.SellerServiceImplementation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SellerServiceTest {
    @Mock
    ISellerRepository sellerRepository;

    @Mock
    ICustomerRepository customerRepository;

    @InjectMocks
    SellerServiceImplementation sellerServiceImplementation;

    ResponsePostDTO responsePostDTO;

    @BeforeEach
    public void setUp() {
        List<PostDTO> postDTOS = new ArrayList<>();
        postDTOS.add(PostDTO.builder()
                .sellerId(2)
                .postId(1)
                .date(LocalDate.of(2023, 1, 1))
                .price(1000)
                .build());

        responsePostDTO = new ResponsePostDTO(1, postDTOS);
    }


    @Test
    @DisplayName("Verificar que el tipo de ordenamiento por fecha exista")
    public void getPostsFromFollowingWithTwoWeeksOldTest() {
        // Arrange
        List<Integer> sellersIds = new ArrayList<>();
        sellersIds.add(2);
        sellersIds.add(3);
        sellersIds.add(4);

        Customer customer = new Customer();
        customer.setUserId(1);

        Map<Integer, List<Post>> postsMap = new HashMap<>();
//        postsMap.put(1, new Post());

        // Act
        when(customerRepository.findCustomerById(1)).thenReturn(customer);
        when(sellerRepository.findPostsByFollowing(customer.getSellers())).thenReturn(postsMap);

        ResponsePostDTO responseReal =  sellerServiceImplementation.getPostsFromFollowingWithTwoWeeksOld(1, "date_asc");

        // Assert
        Assertions.assertEquals(responsePostDTO, responseReal);
    }
}
