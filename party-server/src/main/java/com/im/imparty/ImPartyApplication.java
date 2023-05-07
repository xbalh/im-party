package com.im.imparty;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@Api(tags = "test")
@RestController
@EnableFeignClients(basePackages = {"com.im.imparty.api.music"})
@EnableCaching
@MapperScan(value = {"com.im.imparty.**.mapper"})
@SpringBootApplication
public class ImPartyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImPartyApplication.class, args);
    }


    @ApiOperation("method")
    @GetMapping("/hello")
    public String hello() {
        return "hello 登陆人：" + SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
