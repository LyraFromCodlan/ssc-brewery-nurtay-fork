package guru.sfg.brewery.web.controllers;

import guru.sfg.brewery.repositories.BeerInventoryRepository;
import guru.sfg.brewery.repositories.BeerOrderRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.repositories.CustomerRepository;
import guru.sfg.brewery.services.BeerService;
import guru.sfg.brewery.services.BreweryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public class BaseIntegrationTest{
    @Value("${spring.security.user.name}")
    String name;

    @Value("${spring.security.user.password}")
    String password;


    @Autowired
    WebApplicationContext applicationContext;

    public MockMvc mockMvc;

    @BeforeEach
    public void setUpMockMvcFromAppContext(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                //                applying spring security in enables respective dependency in tests context. Comment next line to disable security in testing
                .apply(springSecurity())
                .build();
    }

    public static Stream<Arguments> getStreamAllUsers(){
        return Stream.of(
                Arguments.of("SPRING","SPRING"),
                Arguments.of("Derpy","muffinz_power")
        );
    }
    public static Stream<Arguments> getStreamNotAdmin(){
        return Stream.of(
                Arguments.of("Bonbon","secret"),
                Arguments.of("Scott","SCOTT"),
                Arguments.of("Fluttershy","wabbit")
        );
    }
}
