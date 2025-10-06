package org.lukasz.faktury;

import com.vaadin.flow.component.page.AppShellConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class FakturyApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(FakturyApplication.class, args);
    }
}
