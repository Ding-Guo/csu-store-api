package org.csu.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.csu.api.persistence")
public class CsuMallApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsuMallApiApplication.class, args);
    }

}
