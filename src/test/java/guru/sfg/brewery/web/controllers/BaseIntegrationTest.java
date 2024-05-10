package guru.sfg.brewery.web.controllers;

import guru.sfg.brewery.repositories.BeerInventoryRepository;
import guru.sfg.brewery.repositories.BeerOrderRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.repositories.CustomerRepository;
import guru.sfg.brewery.services.BeerService;
import guru.sfg.brewery.services.BreweryService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

public class BaseIntegrationTest{
    @Value("${spring.security.user.name}")
    String name;

    @Value("${spring.security.user.password}")
    String password;


    @Autowired
    WebApplicationContext applicationContext;

    MockMvc mockMvc;
    @MockBean
    BeerRepository beerRepository;
    @MockBean
    BeerOrderRepository beerOrderRepository;
    @MockBean
    BeerInventoryRepository beerInventoryRepository;
    @MockBean
    CustomerRepository customerRepository;
    @MockBean
    BeerService beerService;
    @MockBean
    BreweryService breweryService;

    @BeforeEach
    public void setUpMockMvcFromAppContext(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                //                applying spring security in enables respective dependency in tests context. Comment next line to disable security in testing
                .apply(springSecurity())
                .build();
    }
}
