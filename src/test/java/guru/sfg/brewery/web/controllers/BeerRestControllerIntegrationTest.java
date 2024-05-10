package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class BeerRestControllerIntegrationTest extends BaseIntegrationTest{

    @Test
//    WithMockUser is required since we've included spring security into testing
//    @WithMockUser("Lyra Hearthstrings")
    void findBeers() throws Exception{
        mockMvc.perform(get("/api/v1/beer/"))
                .andExpect(status().isOk());
    }

    @Test
//    @WithMockUser("Lyra Hearthstrings")
    void findBeersWithHttpBasic() throws Exception{
        mockMvc.perform(get("/api/v1/beer/"))
                .andExpect(status().isOk());
    }
}
