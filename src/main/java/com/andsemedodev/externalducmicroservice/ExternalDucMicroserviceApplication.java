package com.andsemedodev.externalducmicroservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@OpenAPIDefinition(info = @Info(title = "Generate DUC Microservice",
        version = "v1"
))
public class ExternalDucMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExternalDucMicroserviceApplication.class, args);
    }

}
