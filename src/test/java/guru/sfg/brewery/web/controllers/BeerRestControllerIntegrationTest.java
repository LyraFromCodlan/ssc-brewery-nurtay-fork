package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class BeerRestControllerIntegrationTest extends BaseIntegrationTest{


    @Test
    void deleteBeer() throws Exception {
        mockMvc.perform(delete("/api/v1/beer/4fd5e1fb-85c3-43b7-a1b6-d4d15674635a")
                .header("Api-key", "Derpy")
                .header("Api-secret", "muffinz_power")
        ).andExpect(status().isOk());

    }
//    WithMockUser is required since we've included spring security into testing
//    @WithMockUser("Lyra Hearthstrings")
    void findBeers() throws Exception{
        mockMvc.perform(get("/api/v1/beer/"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeersById() throws Exception{
        mockMvc.perform(get("/api/v1/beer/4fd5e1fb-85c3-43b7-a1b6-d4d15674635a"))
                .andExpect(status().isOk());
    }
    @Test
    void findBeerByUPC() throws Exception{
        mockMvc.perform(get("/api/v1/beerUpc/0631234300019"))
                .andExpect(status().isOk());
    }
}
