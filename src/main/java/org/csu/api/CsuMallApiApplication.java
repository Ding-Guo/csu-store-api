package org.csu.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan("org.csu.api.persistence")
public class CsuMallApiApplication extends SpringBootServletInitializer {
    @Override

    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

        return application.sources(CsuMallApiApplication.class);

    }

    public static void main(String[] args) {

        SpringApplication.run(CsuMallApiApplication.class, args);
    }

}
