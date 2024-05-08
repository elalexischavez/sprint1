package org.example.sprint1.service;

import org.example.sprint1.dto.CountFollowersDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.sprint1.dto.BasicSellerDTO;
import org.example.sprint1.dto.FollowedSellersDTO;
import org.example.sprint1.dto.SellerFollowerDto;
import org.example.sprint1.entity.Customer;
import org.example.sprint1.entity.Seller;
import org.example.sprint1.exception.BadRequestException;
import org.example.sprint1.exception.NotFoundException;
import org.example.sprint1.repository.ICustomerRepository;
import org.example.sprint1.repository.ISellerRepository;
import org.example.sprint1.service.follow.FollowService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
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
    @DisplayName("Verificar que la cantidad de seguidores de un usuario sea correcta")
    public void testCountFollowersToUser() {
        //Arrange
        Seller seller = new Seller();
        seller.setSellerId(1);
        seller.setSellerName("ElectroJoaquin");
        seller.setFollowers(Arrays.asList(1,2,3));
        when(sellerRepository.getSellerById(1)).thenReturn(seller);
        //Act and Assert
        assertDoesNotThrow(() -> followService.countFollowers(1));
    }
    @Test
    @DisplayName("Verificar cuando no se encuentra el vendedor")
    public void testCountFollowersToUserWithNotFound() throws NotFoundException {
        //Arrange
        when(sellerRepository.getSellerById(anyInt())).thenReturn(null);
        //Act and Assert
        Assertions.assertThrows(NotFoundException.class, () -> followService.countFollowers(anyInt()));
    }
    @Test
    @DisplayName("Validate getSellerFollowers with valid order")
    public void testGetSellerFollowersWithValidOrder() {
        // Arrange
        Seller seller = new Seller();
        seller.setSellerName("seller1");
        when(sellerRepository.getSellerById(anyInt())).thenReturn(seller);

        Customer customer1 = new Customer();
        customer1.setUserId(1);
        customer1.setUserName("customer1");
        customer1.setSellers(Arrays.asList(1, 2, 3));

        Customer customer2 = new Customer();
        customer2.setUserId(2);
        customer2.setUserName("customer2");
        customer2.setSellers(Arrays.asList(4, 5, 6));

        when(customerRepository.getCustomersThatFollowsSellersById(anyInt())).thenReturn(Arrays.asList(customer1, customer2));

        // Act
        assertDoesNotThrow(() -> followService.getSellerFollowers(1, "name_asc"));
        assertDoesNotThrow(() -> followService.getSellerFollowers(1, "name_desc"));
    }

    @Test
    @DisplayName("Validate getSellerFollowers with valid order")
    public void testGetSellerFollowersWithInvalidOrder() {
        // Arrange
        Seller seller = new Seller();
        seller.setSellerName("seller1");
        when(sellerRepository.getSellerById(anyInt())).thenReturn(seller);
        when(customerRepository.getCustomersThatFollowsSellersById(anyInt())).thenReturn(Arrays.asList(new Customer(), new Customer()));

        // Act and Assert
        assertThrows(BadRequestException.class, () -> followService.getSellerFollowers(1, "invalid_order"));
    }

    @Test
    @DisplayName("Validate getSellerFollowers sorting by name ascending")
    public void testGetSellerFollowersWithAscendingOrder() {
        // Arrange
        Seller seller = new Seller();
        seller.setSellerName("seller1");
        when(sellerRepository.getSellerById(anyInt())).thenReturn(seller);

        Customer customer1 = new Customer();
        customer1.setUserId(1);
        customer1.setUserName("customerB");
        customer1.setSellers(Arrays.asList(1, 2, 3));

        Customer customer2 = new Customer();
        customer2.setUserId(2);
        customer2.setUserName("customerA");
        customer2.setSellers(Arrays.asList(4, 5, 6));

        when(customerRepository.getCustomersThatFollowsSellersById(anyInt())).thenReturn(Arrays.asList(customer1, customer2));

        // Act
        SellerFollowerDto result = followService.getSellerFollowers(1, "name_asc");

        // Assert
        assertEquals("customerA", result.getFollowers().get(0).getUserName());
        assertEquals("customerB", result.getFollowers().get(1).getUserName());
    }

    @Test
    @DisplayName("Validate getSellerFollowers sorting by name descending")
    public void testGetSellerFollowersWithDescendingOrder() {
        // Arrange
        Seller seller = new Seller();
        seller.setSellerName("seller1");
        when(sellerRepository.getSellerById(anyInt())).thenReturn(seller);

        Customer customer1 = new Customer();
        customer1.setUserId(1);
        customer1.setUserName("customerA");
        customer1.setSellers(Arrays.asList(1, 2, 3));

        Customer customer2 = new Customer();
        customer2.setUserId(2);
        customer2.setUserName("customerB");
        customer2.setSellers(Arrays.asList(4, 5, 6));

        when(customerRepository.getCustomersThatFollowsSellersById(anyInt())).thenReturn(Arrays.asList(customer1, customer2));

        // Act
        SellerFollowerDto result = followService.getSellerFollowers(1, "name_desc");

        // Assert
        assertEquals("customerB", result.getFollowers().get(0).getUserName());
        assertEquals("customerA", result.getFollowers().get(1).getUserName());
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

        List<BasicSellerDTO> sellersFollowed = Stream.of(
                        new BasicSellerDTO(1, "Juan", null, null),
                        new BasicSellerDTO(2, "Andres", null, null),
                        new BasicSellerDTO(3, "Diego", null, null)
                )
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

    @Test
    @DisplayName("Validate getSellerFollowed with valid order")
    public void GetSellerFollowedWithValidOrderTest() {
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
                .sorted(Comparator.comparing(BasicSellerDTO::getSellerName).reversed()).collect(Collectors.toList());;

        List<Integer> sellersIdFollowed = List.of(1, 2, 3);

        Customer customer = new Customer();
        customer.setUserId(1);
        customer.setUserName("Customer1");
        customer.setSellers(sellersIdFollowed);

        FollowedSellersDTO expectedResponse = new FollowedSellersDTO();
        expectedResponse.setUserId(customer.getUserId());
        expectedResponse.setCustomerName(customer.getUserName());


        // Act
        when(customerRepository.findCustomerById(1)).thenReturn(customer);
        when(sellerRepository.getCustomersThatFollowsSellersById(anyInt())).thenReturn(sellers);


        // Assert
        assertDoesNotThrow(() -> followService.getFollowedSellers(1, "name_asc"));
        assertDoesNotThrow(() -> followService.getFollowedSellers(1, "name_desc"));
    }

    @Test
    public void testGetFollowedSellersWithInvalidOrder() {
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
                .sorted(Comparator.comparing(BasicSellerDTO::getSellerName).reversed()).collect(Collectors.toList());;

        List<Integer> sellersIdFollowed = List.of(1, 2, 3);

        Customer customer = new Customer();
        customer.setUserId(1);
        customer.setUserName("Customer1");
        customer.setSellers(sellersIdFollowed);

        FollowedSellersDTO expectedResponse = new FollowedSellersDTO();
        expectedResponse.setUserId(customer.getUserId());
        expectedResponse.setCustomerName(customer.getUserName());

        // Act
        when(customerRepository.findCustomerById(1)).thenReturn(customer);
        when(sellerRepository.getCustomersThatFollowsSellersById(anyInt())).thenReturn(sellers);

        // Assert
        assertThrows(BadRequestException.class, () -> followService.getFollowedSellers(1, "invalid_order"));
    }

}
