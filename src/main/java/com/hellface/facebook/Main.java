package com.hellface.facebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Johno Crawford (johno@hellface.com)
 */
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.hellface.facebook"})
public class Main {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }
}
