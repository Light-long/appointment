package com.terminus.cmn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.terminus")
public class CmnApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmnApplication.class, args);
    }
}
