package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerOrderRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.controllers.BaseIntegrationTest;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BeerRestControllerIntegrationTest extends BaseIntegrationTest{

    @Autowired
    BeerRepository beerRepository;
    @Autowired
    BeerOrderRepository beerOrderRepository;

    @DisplayName("Delete Tests")
    @Nested
    class DeleteTests{

        public Beer beerToDelete(){
            Random random = new Random();

            return beerRepository.saveAndFlush(
                    Beer. builder()
                            .beerName("Delete Me Beer")
                            .beerStyle(BeerStyleEnum.IPA)
                            .minOnHand(12)
                            .quantityToBrew(200)
                            .upc(String.valueOf(random.nextInt(99999999)))
                            .build()
            );
        }

        @Test
        void deleteBeerUrlBadCreds() throws Exception{
            mockMvc.perform(delete("/api/v1/beer/"+beerToDelete().getId())
                            .param("Api-Key","Bonbon").param("Api-Secret", "guruXXXX"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void deleteBeerUrl() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/"+beerToDelete().getId())
                            .param("Api-Key", "Bonbon").param("Api-Secret", "secret"))
                    .andExpect(status().isOk());
        }

        @Test
        void deleteBeerBadCreds() throws Exception{
            mockMvc.perform(delete("/api/v1/beer/"+beerToDelete().getId())
                            .header("Api-Key","Bonbon").header("Api-Secret", "guruXXXX"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void deleteBeer() throws Exception {
            mockMvc.perform(delete("/api/v1/beer/"+beerToDelete().getId())
                            .header("Api-Key", "Bonbon").header("Api-Secret", "secret"))
                    .andExpect(status().isOk());
        }

        @Test
        void deleteBeerHttpBasic() throws Exception{
            mockMvc.perform(delete("/api/v1/beer/"+beerToDelete().getId())
                            .with(httpBasic("Derpy", "muffinz_power")))
                    .andExpect(status().is2xxSuccessful());
        }

        @Test
        void deleteBeerHttpBasicUserRole() throws Exception{
            mockMvc.perform(delete("/api/v1/beer/"+beerToDelete().getId())
                            .with(httpBasic("User", "USER")))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerHttpBasicCustomerRole() throws Exception{
            mockMvc.perform(delete("/api/v1/beer/"+beerToDelete().getId())
                            .with(httpBasic("Scott", "SCOTT")))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerNoAuth() throws Exception{
            mockMvc.perform(delete("/api/v1/beer/"+beerToDelete().getId()))
                    .andExpect(status().isUnauthorized());
        }
    }


    @Test
        //    WithMockUser is required since we've included spring security into testing
//    @WithMockUser("Lyra Hearthstrings")
    void findBeers() throws Exception{
        mockMvc.perform(get("/api/v1/beer/"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerById() throws Exception{
        Beer beer = beerRepository.findAll().get(0);

        mockMvc.perform(get("/api/v1/beer/"+beer.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerByUpc() throws Exception{
        mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerByAdmin() throws Exception{
        mockMvc.perform(get("/beers").param("beerName", "")
                .with(httpBasic("Derpy","muffinz_power")))
                .andExpect(status().isOk());
    }
}
