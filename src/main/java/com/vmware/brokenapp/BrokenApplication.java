package com.vmware.brokenapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class BrokenApplication {
    public static void main(String[] args) {
        SpringApplication.run(BrokenApplication.class, args);
    }
}

