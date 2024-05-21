package guru.sfg.brewery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize ->
                    authorize
                            .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                            .antMatchers("/beers", "/beers/find").permitAll()
                            .antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll()
                            .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll()
                    )
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().and()
                .httpBasic();
    }


//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("Bonbon")
//                .password("secret")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("Derpy")
//                .password("muffinz")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, user);
//    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        authenticate 2 staffs members: admin and cash machine operator
        auth.inMemoryAuthentication()
                .withUser("Bonbon")
//                Added encoder configuration so there is no need to mention encoding algorithm in brackets
//                .password("{noop}secret")
                .password("secret")
                .roles("ADMIN")
                .and()
                .withUser("Derpy")
//                Added encoder configuration so there is no need to mention encoding algorithm in brackets
//                .password("{noop}muffinz")
                .password("muffinz")
                .roles("USER");
//        memory in-build customer
        auth.inMemoryAuthentication()
                .withUser("Fluttershy")
//        Added encoder configuration so there is no need to mention encoding algorithm in brackets. Don't use algorithm name in brackets if encoder is already configured
//                .password("{noop}wabbit")
                .password("wabbit")
                .roles("CUSTOMER");
    }
}
