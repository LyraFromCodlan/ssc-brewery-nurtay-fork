package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.web.controllers.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BreweryRestControllerTest extends BaseIntegrationTest {

    @Test
    void listBreweriesCustomer() throws Exception{
        mockMvc.perform(
                get("/brewery/breweries")
                        .with(httpBasic("Bonbon","secret")))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    void listBreweriesAdmin() throws Exception{
        mockMvc.perform(
                get("/brewery/breweries")
                        .with(httpBasic("Derpy","muffinz_power")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void listBreweriesUser() throws Exception{
        mockMvc.perform(
                get("/brewery/breweries")
                        .with(httpBasic("User","USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void listBreweriesNoAuth() throws Exception{
        mockMvc.perform(                get("/brewery/breweries"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void gerBreweriesJsonCustomer() throws Exception{
        mockMvc.perform(
                        get("/brewery/api/v1/breweries")
                                .with(httpBasic("Fluttershy","wabbit")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void gerBreweriesJsonAdmin() throws Exception{
        mockMvc.perform(
                        get("/brewery/api/v1/breweries")
                                .with(httpBasic("Derpy","muffinz_power")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void gerBreweriesJsonUser() throws Exception{
        mockMvc.perform(
                        get("/brewery/api/v1/breweries")
                                .with(httpBasic("User","USER")))
                .andExpect(status().isForbidden());
    }

    @Test
    void gerBreweriesJsonNoAuth() throws Exception{
        mockMvc.perform(get("/brewery/api/v1/breweries"))
                .andExpect(status().isUnauthorized());
    }

}
