package org.lukasz.faktury.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lukasz.faktury.exceptions.UserException;
import org.lukasz.faktury.nipapi.ApiConnection;
import org.lukasz.faktury.seller.Seller;
import org.lukasz.faktury.seller.SellerDto;
import org.lukasz.faktury.seller.SellerService;
import org.lukasz.faktury.user.dto.UserRequest;
import org.lukasz.faktury.user.dto.UserResponse;
import org.lukasz.faktury.utils.confirmationtoken.activationtoken.ActivationTokenService;
import org.lukasz.faktury.utils.validation.Validation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;
    @Mock
    private Validation validation;
    @Mock
    private ApiConnection connection;
    @Mock
    private SellerService sellerService;
    @Mock
    private ActivationTokenService activationTokenService;
    @InjectMocks
    private UserServiceImpl userService;



    @Test
    void givenValidRequest_whenRegister_thenUserIsSavedAndTokenCreated() {
        // given
        UserRequest request = new UserRequest("7151536825@test.pl", "pass", "7151536825");

        SellerDto sellerDto = new SellerDto("Dolce Vita MARTA BĄK", "7151536825", "060293389", "Urzędów", "23-250", "ul. Rynek", "5");

        Seller seller = new Seller();
        seller.setId(1L);
        seller.setName("Dolce Vita MARTA BĄK");

        User entity = new User();
        entity.setEmail("7151536825@test.pl");

        User savedEntity = new User();
        savedEntity.setId(1L);
        savedEntity.setEmail("7151536825@test.pl");
        savedEntity.setSeller(seller);

        UserResponse response = new UserResponse("7151536825@test.pl");


        doNothing().when(validation).validation(request);
        when(repository.findByEmail("7151536825@test.pl")).thenReturn(Optional.empty());

        when(sellerService.save(sellerDto)).thenReturn(seller);
        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toResponse(savedEntity)).thenReturn(response);

        // when
        UserResponse result = userService.register(request, sellerDto);

        // then
        Assertions.assertEquals("7151536825@test.pl", result.email());
        verify(validation).validation(request);
        verify(sellerService).save(sellerDto);
        verify(repository).save(entity);
        verify(activationTokenService).createToken(savedEntity);
        verify(mapper).toResponse(savedEntity);
    }
    @Test
    void givenInvalidRequest_whenRegister_thenUserIsSavedAndTokenCreated() {
        // given

        UserRequest request = mock(UserRequest.class);
        SellerDto sellerDto= mock(SellerDto.class);
        User entity = mock(User.class);

        doNothing().when(validation).validation(request);
        when(repository.findByEmail(any())).thenReturn(Optional.of(entity));

        // when
        assertThrows(UserException.class, () -> userService.register(request, sellerDto));

        //then
        verify(sellerService, never()).save(any());
        verify(mapper, never()).toEntity(any());
        verify(mapper, never()).toResponse(any());
        verify(repository, never()).save(any());
        verify(activationTokenService, never()).createToken(any());
        verify(mapper, never()).toResponse(any());


    }

}

