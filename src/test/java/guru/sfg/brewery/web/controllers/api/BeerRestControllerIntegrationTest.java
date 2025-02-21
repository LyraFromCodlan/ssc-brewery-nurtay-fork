package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerOrderRepository;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.controllers.BaseIntegrationTest;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BaseIntegrationTest#getStreamAllUsers")
        void deleteNoopBeerNoAuth(String user, String password) throws Exception{
            mockMvc.perform(delete("/api/v1/beer/"+beerToDelete().getId())
                            .with(user(user).password(password)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteBeerNoAuth() throws Exception{
            mockMvc.perform(delete("/api/v1/beer/"+beerToDelete().getId()))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("Init new Form")
    @Nested
    class InitNewForm {

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BaseIntegrationTest#getStreamAllUsers")
        void initCreationFormAuth(String user, String password) throws Exception{
            mockMvc.perform(get("/beers/new").with(httpBasic(user,password)))
                    .andExpect(status().isOk())
                    .andExpect(view().name("beers/createBeer"))
                    .andExpect(model().attributeExists("beer"));
        }

        @Test
        void initCreationFormNotAuth() throws Exception{
            mockMvc.perform(delete("/beers/new"))
                    .andExpect(status().isUnauthorized());
        }
    }


    @DisplayName("List Beers")
    @Nested
    class ListBeers {

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BaseIntegrationTest#getStreamAllUsers")
        void initCreationFormAuth(String user, String password) throws Exception{
            mockMvc.perform(get("/api/v1/beer").with(httpBasic(user,password)))
                    .andExpect(status().isOk());
        }
    }

    @DisplayName("Get Beer BY ID")
    @Nested
    class GetBeerById {
        public Beer beerToFind(){
            Random random = new Random();

            return beerRepository.saveAndFlush(
                    Beer. builder()
                            .beerName("Scoobie-Doo beer")
                            .beerStyle(BeerStyleEnum.LAGER)
                            .minOnHand(18)
                            .quantityToBrew(420)
                            .upc(String.valueOf(random.nextInt(99999999)))
                            .build()
            );
        }


        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BaseIntegrationTest#getStreamAllUsers")
        void findBeerById(String user, String password) throws Exception{
            mockMvc.perform(get("/api/v1/beer/"+beerToFind().getId()).with(httpBasic(user,password)))
                    .andExpect(status().isOk());
        }
    }

    @DisplayName("Get Beer BY UPC")
    @Nested
    class GetBeerByUpc {
        @Test
        void findBeerByUpc() throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/"+"0631234200036")).andExpect(status().isUnauthorized());
        }


        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BaseIntegrationTest#getStreamAllUsers")
        void findBeerByUpcAuth(String user, String password) throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/"+"0631234200036").with(httpBasic(user,password)))
                    .andExpect(status().isOk());
        }
    }

//    @Test
//    void findBeerByAdmin() throws Exception{
//        mockMvc.perform(get("/beers").param("beerName", "")
//                .with(httpBasic("Derpy","muffinz_power")))
//                .andExpect(status().isOk());
//    }
}
