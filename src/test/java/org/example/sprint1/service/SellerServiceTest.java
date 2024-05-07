package org.example.sprint1.service;

import org.example.sprint1.exception.BadRequestException;
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

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SellerServiceTest {
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
}
