package org.lukasz.faktury.items;

import java.util.List;

public interface InvoiceItemsService {
    List<String> tax();
    List<String>unit();
    int taxValue(String tax);
}
