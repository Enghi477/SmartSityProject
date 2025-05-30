package ru.top.smartcity;

import com.github.javafaker.Faker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import java.time.Duration;

@SpringBootApplication
public class SmartCityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartCityApplication.class, args);
    }
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
        return  new HiddenHttpMethodFilter();
    }

    @Bean
    public Faker faker(){
        return new Faker();
    }

//    @Bean
//    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        return builder
//                .setConnectTimeout(Duration.ofSeconds(5))
//                .setReadTimeout(Duration.ofSeconds(5))
//                .build();
//    }
}
