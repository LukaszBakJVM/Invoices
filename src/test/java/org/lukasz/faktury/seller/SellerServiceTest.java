package org.lukasz.faktury.seller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lukasz.faktury.exceptions.NipAlreadyRegisteredException;
import org.lukasz.faktury.seller.dto.SellerDto;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SellerServiceTest {
    @Mock
    private SellerRepo repository;
    @Mock
    private SellerMapper mapper;

    @InjectMocks
    private SellerServiceImp sellerServiceImp;

    @Test
    void whenCreateNewSeller_AndSellerHaveAccount() {

        //given
        Seller seller = mock(Seller.class);
        SellerDto sellerDto = new SellerDto("exist", "7151356789", "000000", "city", "23-000", "street", "5");

        when(repository.findByNipAndName(anyString(), anyString())).thenReturn(Optional.of(seller));

        //then
        assertThrows(NipAlreadyRegisteredException.class, () -> sellerServiceImp.save(sellerDto));
    }


}
