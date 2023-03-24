package com.im.imparty;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 */
@MapperScan(value = {"com.im.imparty.**.mapper"})
@SpringBootApplication
public class ImPartyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImPartyApplication.class, args);
    }

}
