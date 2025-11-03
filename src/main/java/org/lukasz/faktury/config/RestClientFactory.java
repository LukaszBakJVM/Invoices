package org.lukasz.faktury.config;

import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RestClientFactory {
    @Value("${apiinvoice}")
    private String apiInvoice;

    public RestClient forCurrentUser() {


        String user = (String) VaadinSession.getCurrent().getAttribute("email");


        String pass = (String) VaadinSession.getCurrent().getAttribute("pass");
        return RestClient.builder().baseUrl(apiInvoice).defaultHeaders(headers -> headers.setBasicAuth(user, pass)).build();
    }
}

