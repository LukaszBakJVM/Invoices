package org.lukasz.faktury.buyer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lukasz.faktury.buyer.dto.BuyerDto;
import org.lukasz.faktury.nipapi.ApiConnection;
import org.lukasz.faktury.nipapi.ceidgapi.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BuyerServiceTest {
    @Mock
    private BuyerMapper buyerMapper;
    @Mock
    private BuyerRepo buyerRepo;
    @Mock
    private ApiConnection connection;
    @InjectMocks
    BuyerServiceImpl buyerService;


    @Test
    void shouldReturnExistingBuyer_WhenBuyerExistsByNip() {

        //given
        Buyer buyer = new Buyer();
        buyer.setId(1L);
        buyer.setCity("city");

        Buyer buyer1 = new Buyer();
        buyer1.setId(2L);
        buyer1.setCity("city1");


        BuyerDto buyerDto = new BuyerDto("name", "6351234577", "787273", "city", "00-000", "street", "8");
        when(buyerRepo.findByNip(anyString())).thenReturn(List.of(buyer, buyer1));

        when(buyerMapper.entityToDto(any())).thenReturn(buyerDto);

        //when
        buyerService.findByNipAndSave("7134567809");

        //then

        verify(buyerRepo).findByNip(anyString());
        verify(buyerMapper).entityToDto(any());
        verify(connection, never()).result(anyString());
        verify(buyerMapper, never()).toEntity(any());
        verify(buyerRepo, never()).save(any());



    }


    @Test
    void shouldFetchAndSaveNewBuyer_WhenBuyerNotFoundByNip() {


        //given
        Buyer buyer = new Buyer();
        buyer.setId(1L);
        buyer.setCity("city");
        CeidgResult ceidgResult = new CeidgResult("name", new BusinessAddress("street", "5", "00-00", "warszawa"), new Owner("7653456789", "56789900"), "aktywny", new CorrespondenceAddress("street", "5", "00-000", "warszawa"));

        CeidgNipApiResponse ceidgResponse = new CeidgNipApiResponse(List.of(ceidgResult));

        Buyer buyer1 = new Buyer();
        buyer1.setId(2L);
        buyer1.setCity("city1");

        BuyerDto buyerDto = new BuyerDto("name", "6351234577", "787273", "city", "00-000", "street", "8");


        when(buyerRepo.findByNip(anyString())).thenReturn(List.of());
        when(connection.result(anyString())).thenReturn(ceidgResponse);
        when(buyerMapper.toEntity(any())).thenReturn(buyer);
        when(buyerRepo.save(any())).thenReturn(buyer);
        when(buyerMapper.entityToDto(any())).thenReturn(buyerDto);


        //when
        buyerService.findByNipAndSave("7134567809");

        //then
        verify(connection).result(anyString());
        verify(buyerMapper).entityToDto(any());
        verify(buyerRepo).save(any());
        verify(buyerMapper).entityToDto(any());


    }

    @Test
    void shouldFindBuyer_BeforeCreateInvoices() {
        BuyerDto buyerDto = new BuyerDto("companyName", "1234567890", null, null, null, null, null);

        Buyer buyer = mock(Buyer.class);
        when(buyerRepo.findByNipAndName(anyString(), anyString())).thenReturn(Optional.of(buyer));

        buyerService.findBuyer(buyerDto);


        verify(buyerRepo).findByNipAndName(anyString(), anyString());

    }

    @Test
    void shouldNotCreateBuyer_BeforeCreateInvoices() {
        //given

        BuyerDto buyerDto = new BuyerDto("companyName", "1234567890", null, null, null, null, null);
        Buyer buyer = mock(Buyer.class);
        when(buyerRepo.findByNipAndName(anyString(), anyString())).thenReturn(Optional.of(buyer));

        //when
        buyerService.findByNipAndNameAndSave(buyerDto);


        //then
        verify(buyerRepo).findByNipAndName(anyString(), anyString());
        verify(buyerRepo, never()).save(any());

    }

    @Test
    void shouldCreateBuyer_BeforeCreateInvoices() {
        BuyerDto buyerDto = new BuyerDto("companyName", "1234567890", null, null, null, null, null);

        Buyer buyer = mock(Buyer.class);
        when(buyerRepo.findByNipAndName(anyString(), anyString())).thenReturn(Optional.empty());
        when(buyerMapper.toEntity(any())).thenReturn(buyer);
        when(buyerRepo.save(any())).thenReturn(buyer);

        buyerService.findByNipAndNameAndSave(buyerDto);

        verify(buyerMapper).toEntity(any());
        verify(buyerRepo).save(any());

    }

    @Test
    void shouldFindBuyerDto_BeforeCreateInvoices(){
       //given
        BuyerDto buyerDto = new BuyerDto("companyName", "1234567890", null, null, null, null, null);
        Buyer buyer = mock(Buyer.class);

        when(buyerRepo.findByNipAndName(anyString(),anyString())).thenReturn(Optional.of(buyer));
        when(buyerMapper.entityToDto(any())).thenReturn(buyerDto);

       //when
        buyerService.findByNipAndName(anyString(),anyString());

       //then
        verify(buyerRepo).findByNipAndName(anyString(),anyString());
        verify(buyerMapper).entityToDto(any());





    }
}
