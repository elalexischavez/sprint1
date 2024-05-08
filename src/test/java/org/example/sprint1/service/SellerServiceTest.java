package org.example.sprint1.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.ConstraintViolationException;
import org.example.sprint1.dto.PostDTO;
import org.example.sprint1.dto.ResponsePostDTO;
import org.example.sprint1.entity.Customer;
import org.example.sprint1.entity.Post;
import org.example.sprint1.exception.BadRequestException;
import org.example.sprint1.exception.NotFoundException;
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
import java.util.*;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SellerServiceTest {
    @Mock
    ISellerRepository sellerRepository;

    @Mock
    ICustomerRepository customerRepository;

    @InjectMocks
    SellerServiceImplementation sellerServiceImplementation;

    ResponsePostDTO responsePostDTOAsc;
    ResponsePostDTO responsePostDTODesc;
    Customer customer;
    Map<Integer, List<Post>> postsMap = new HashMap<>();

    @BeforeEach
    public void setup() {
        int userId = 1;
        List<PostDTO> postDTOS = new ArrayList<>();
        postDTOS.add(PostDTO.builder()
                .sellerId(2)
                .postId(100)
                .date(LocalDate.of(2024, 4, 30))
                .category(1000)
                .price(1000)
                .build()
        );

        postDTOS.add(PostDTO.builder()
                .sellerId(3)
                .postId(101)
                .date(LocalDate.of(2024, 5, 2))
                .category(2000)
                .price(1000)
                .build()
        );

        List<PostDTO> reversedPostDTOS = new ArrayList<>(postDTOS);
        Collections.reverse(reversedPostDTOS);

        responsePostDTOAsc = new ResponsePostDTO(userId, postDTOS);
        responsePostDTODesc = new ResponsePostDTO(userId, reversedPostDTOS);


        customer = Customer.builder()
                .userId(1)
                .userName("Juan")
                .sellers(Arrays.asList(2, 3))
                .build();

        postsMap.put(2, List.of(
                        Post.builder()
                                .postId(100)
                                .date(LocalDate.of(2024, 4, 30))
                                .category(1000)
                                .price(1000)
                                .build()
                )
        );

        postsMap.put(3, List.of(
                        Post.builder()
                                .postId(101)
                                .date(LocalDate.of(2024, 5, 2))
                                .category(2000)
                                .price(1000)
                                .build()
                )
        );
    }

    @Test
    @DisplayName("T-0005 Verificar que el tipo de ordenamiento por fecha exista")
    public void checkDateOrderExistsTest() {
        // Act
        when(customerRepository.findCustomerById(1)).thenReturn(customer);
        when(sellerRepository.findPostsByFollowing(customer.getSellers())).thenReturn(postsMap);

        // Assert
        ResponsePostDTO responseDateAsc = sellerServiceImplementation.getPostsFromFollowingWithTwoWeeksOld(1, "date_asc");
        ResponsePostDTO responseDateDesc = sellerServiceImplementation.getPostsFromFollowingWithTwoWeeksOld(1, "date_desc");

        Assertions.assertDoesNotThrow(() -> responseDateAsc);
        Assertions.assertDoesNotThrow(() -> responseDateDesc);
    }

    @Test
    @DisplayName("T-0005 Verificar que el ordenamiento por fecha no existe")
    public void checkOrderExistsTest() {
        // Act
        when(customerRepository.findCustomerById(1)).thenReturn(customer);
        when(sellerRepository.findPostsByFollowing(customer.getSellers())).thenReturn(postsMap);

        // Assert
        Assertions.assertThrows(BadRequestException.class, () -> sellerServiceImplementation.getPostsFromFollowingWithTwoWeeksOld(1, "asdf"));
    }

    @Test
    @DisplayName("T-0006 Verificar el correcto ordenamiento ascendente y descendente por fecha")
    public void dateOrderingCorrectTest() {
        // Act
        when(customerRepository.findCustomerById(1)).thenReturn(customer);
        when(sellerRepository.findPostsByFollowing(customer.getSellers())).thenReturn(postsMap);

        ResponsePostDTO responseDateAsc = sellerServiceImplementation.getPostsFromFollowingWithTwoWeeksOld(1, "date_asc");
        ResponsePostDTO responseDateDesc = sellerServiceImplementation.getPostsFromFollowingWithTwoWeeksOld(1, "date_desc");

        // Assert
        Assertions.assertEquals(responsePostDTOAsc, responseDateAsc);
        Assertions.assertEquals(responsePostDTODesc, responseDateDesc);
    }


    @Test
    @DisplayName("T-0006 Verificar id de usuario no existe")
    public void checkUserIdExistsTest() {
        // Act
        when(customerRepository.findCustomerById(anyInt())).thenReturn(null);

        // Assert
        Assertions.assertThrows(NotFoundException.class, () -> sellerServiceImplementation.getPostsFromFollowingWithTwoWeeksOld(1, "date_asc"));
    }

}
