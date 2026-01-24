package org.lukasz.faktury.items;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InvoiceItemsServiceImplTest {
    @Mock
    InvoiceItemsRepo invoiceItemsRepo;
    @Mock
    InvoiceItemsMapper invoiceItemsMapper;
    @InjectMocks
    InvoiceItemsServiceImpl invoiceItemsService;
}
