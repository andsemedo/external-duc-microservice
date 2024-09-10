package com.andsemedodev.externalducmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ExternalDucMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExternalDucMicroserviceApplication.class, args);
    }

}
