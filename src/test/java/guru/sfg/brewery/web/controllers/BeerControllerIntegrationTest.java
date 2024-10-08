package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class BeerControllerIntegrationTest extends BaseIntegrationTest{

//    with BCrypt encoder only requests with Derpy User are passed because passoword must be hashed
    @Test
    void initCreationForm() throws Exception{
        mockMvc.perform(get("/beers/new").with(httpBasic("Derpy","muffinz_power")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/createBeer"))
                .andExpect(model().attributeExists("beer"));
    }
    @Test
    void initCreationFormWithAdmin() throws Exception{
        mockMvc.perform(get("/beers/new").with(httpBasic("Bonbon","secret")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/createBeer"))
                .andExpect(model().attributeExists("beer"));
    }

    @Test
    void initCreationFormWithDerpyAdmin() throws Exception{
        mockMvc.perform(get("/beers/new").with(httpBasic("Derpy","muffinz_power")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/createBeer"))
                .andExpect(model().attributeExists("beer"));
    }

    @Test
    void findBeerWithCustomer() throws Exception{
        mockMvc.perform(get("/beers/find").with(httpBasic("Fluttershy","wabbit")))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
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
