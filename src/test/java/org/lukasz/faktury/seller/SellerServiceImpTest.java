package org.lukasz.faktury.seller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lukasz.faktury.exceptions.NipAlreadyRegisteredException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SellerServiceImpTest {
    @Mock
    private SellerRepo repository;
    @Mock
    private SellerMapper mapper;

    @InjectMocks
    private SellerServiceImp sellerServiceImp;


    @Test
    void shouldCreateNewAccount() {

        //given
        Seller sellerEntity = new Seller();
        sellerEntity.setName("exist");
        sellerEntity.setNip("7151356789");
        sellerEntity.setNip("123355");

        SellerDto sellerDto = new SellerDto("exist", "7151356789", "000000", "city", "23-000", "street", "5");

        when(repository.findByNipAndName(anyString(), anyString())).thenReturn(Optional.empty());
        when(mapper.toEntity(sellerDto)).thenReturn(mock(Seller.class));
        when(repository.save(any())).thenReturn(sellerEntity);


        //when
        Seller result = sellerServiceImp.save(sellerDto);


        //then
        Assertions.assertNotNull(result);
        verify(repository).findByNipAndName(anyString(), anyString());
        verify(mapper).toEntity(any());
        verify(repository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenSellerHaveAccount() {

        //given
        Seller seller = mock(Seller.class);
        SellerDto sellerDto = new SellerDto("exist", "7151356789", "000000", "city", "23-000", "street", "5");

        when(repository.findByNipAndName(anyString(), anyString())).thenReturn(Optional.of(seller));

        //then
        assertThrows(NipAlreadyRegisteredException.class, () -> sellerServiceImp.save(sellerDto));
    }


}
