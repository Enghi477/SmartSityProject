package ru.top.smartcity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Java6Assertions.assertThat;

@SpringBootTest
class SmartCityApplicationTests {

    @Test
    void contextLoads() {
    }

}


//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//class SmartCityApplicationTests {
//
//    @Autowired
//    private ApplicationContext context;
//
//    @Test
//    void contextLoads() {
//        assertThat(context).isNotNull();
//    }
//}