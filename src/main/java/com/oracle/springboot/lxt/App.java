package com.oracle.springboot.lxt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.oracle.springboot.lxt.mapper")
@EnableTransactionManagement
public class App {
    public static void main(String[] args) {
        System.out.println(13651);
        SpringApplication.run(App.class, args);
    }
}
