package guru.sfg.brewery.web.controllers;

import guru.sfg.brewery.repositories.BeerInventoryRepository;
import guru.sfg.brewery.repositories.BeerOrderRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.repositories.CustomerRepository;
import guru.sfg.brewery.services.BeerService;
import guru.sfg.brewery.services.BreweryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest
public class BeerControllerIntegrationTest {
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

    @Value("${spring.security.user.name}")
    String name;

    @Value("${spring.security.user.password}")
    String password;


    @BeforeEach
    void setUpMockMvcFromAppContext(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
//                applying spring security in enables respective dependency in tests context. Comment next line to disable security in testing
                .apply(springSecurity())
                .build();
    }

    @Test
//    WithMockUser is required since we've included spring security into testing
    @WithMockUser("Lyra Hearthstrings")
    void findBeers() throws Exception{
        mockMvc.perform(get("/beers/find"))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
    }
    @Test
    void findBeersWithHttpBasic() throws Exception{
        mockMvc.perform(get("/beers/find").with(httpBasic(name,password)))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
    }
}
