package org.example.sprint1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sprint1.dto.BasicSellerDTO;
import org.example.sprint1.dto.FollowedSellersDTO;
import org.example.sprint1.entity.Customer;
import org.example.sprint1.entity.Post;
import org.example.sprint1.entity.Product;
import org.example.sprint1.entity.Seller;
import org.example.sprint1.exception.BadRequestException;
import org.example.sprint1.repository.ICustomerRepository;
import org.example.sprint1.repository.ISellerRepository;
import org.example.sprint1.repository.SellerRepository;
import org.example.sprint1.service.follow.FollowService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FollowServiceTest {
    @Mock
    ICustomerRepository customerRepository;

    @Mock
    ISellerRepository sellerRepository;

    @InjectMocks
    FollowService followService;

    @Test
    @DisplayName("validar la correctamente follow")
    public void validarCorrectamenteFollow() {
        when(sellerRepository.userIdToFollowSeller(235, 101)).thenReturn(false);
        when(customerRepository.userIdToFollowCustomer(235, 101)).thenReturn(false);

        followService.userIdToFollow(235, 101);

        verify(customerRepository, times(1)).userIdToFollowCustomer(235, 101);
        verify(sellerRepository, times(1)).userIdToFollowSeller(235, 101);
    }

    @Test
    @DisplayName("validar de forma incorrecta vendedor de follow")
    public void validarIncorrectamenteVendedorFollow() throws BadRequestException {
        when(sellerRepository.userIdToFollowSeller(235, 90)).thenReturn(true);
        when(customerRepository.userIdToFollowCustomer(235, 90)).thenReturn(false);

        Assertions.assertThrows(BadRequestException.class, () -> followService.userIdToFollow(235, 90));
    }

    @Test
    @DisplayName("validar de forma incorrecta comprador de follow")
    public void validarIncorrectamenteCompradorFollow() throws BadRequestException {
        when(sellerRepository.userIdToFollowSeller(200, 101)).thenReturn(false);
        when(customerRepository.userIdToFollowCustomer(200, 101)).thenReturn(true);

        Assertions.assertThrows(BadRequestException.class, () -> followService.userIdToFollow(200, 101));
    }


    @Test
    @DisplayName("Validar que el orden de la lista de los vendedores que un usuario sigue este ASC")
    public void getFollowedSellersTestAsc() {

        // Arrange
        List<Seller> sellers = List.of(
                new Seller(1, "Juan", null, null),
                new Seller(2, "Andres", null, null),
                new Seller(3, "Diego", null, null)
        );

        List<BasicSellerDTO> sellersFollowed = Stream.of(
                        new BasicSellerDTO(1, "Juan", null, null),
                        new BasicSellerDTO(2, "Andres", null, null),
                        new BasicSellerDTO(3, "Diego", null, null)
                )
                .sorted(Comparator.comparing(BasicSellerDTO::getSellerName)).collect(Collectors.toList());;

        List<Integer> sellersIdFollowed = List.of(1, 2, 3);

        Customer customer = new Customer();
        customer.setUserId(1);
        customer.setUserName("Customer1");
        customer.setSellers(sellersIdFollowed);

        FollowedSellersDTO expectedResponse = new FollowedSellersDTO();
        expectedResponse.setUserId(customer.getUserId());
        expectedResponse.setCustomerName(customer.getUserName());
        expectedResponse.setFollowed(sellersFollowed);

        // Act
        ObjectMapper mapper = new ObjectMapper();

        when(customerRepository.findCustomerById(1)).thenReturn(customer);
        when(sellerRepository.getCustomersThatFollowsSellersById( 1 ) ).thenReturn(sellers);

        FollowedSellersDTO response = followService.getFollowedSellers(1, "name_asc");

        // Assert
        Assertions.assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("Validar que el orden de la lista de los vendedores que un usuario sigue este DESC")
    public void getFollowedSellersTestDesc() {

        // Arrange
        List<Seller> sellers = List.of(
                new Seller(1, "Juan", null, null),
                new Seller(2, "Andres", null, null),
                new Seller(3, "Diego", null, null)
        );

        List<BasicSellerDTO> sellersFollowed = List.of(
                        new BasicSellerDTO(1, "Juan", null, null),
                        new BasicSellerDTO(2, "Andres", null, null),
                        new BasicSellerDTO(3, "Diego", null, null)
                ).stream()
                .sorted(Comparator.comparing(BasicSellerDTO::getSellerName).reversed()).collect(Collectors.toList());;

        List<Integer> sellersIdFollowed = List.of(1, 2, 3);

        Customer customer = new Customer();
        customer.setUserId(1);
        customer.setUserName("Customer1");
        customer.setSellers(sellersIdFollowed);

        FollowedSellersDTO expectedResponse = new FollowedSellersDTO();
        expectedResponse.setUserId(customer.getUserId());
        expectedResponse.setCustomerName(customer.getUserName());
        expectedResponse.setFollowed(sellersFollowed);

        // Act
        ObjectMapper mapper = new ObjectMapper();

        when(customerRepository.findCustomerById(1)).thenReturn(customer);
        when(sellerRepository.getCustomersThatFollowsSellersById( 1 ) ).thenReturn(sellers);

        FollowedSellersDTO response = followService.getFollowedSellers(1, "name_desc");

        // Assert
        Assertions.assertEquals(expectedResponse, response);
    }

}
