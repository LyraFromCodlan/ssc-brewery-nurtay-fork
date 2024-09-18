package guru.sfg.brewery.config;

import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.RestUrlAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncoderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private RestHeaderAuthFilter headerFilter(AuthenticationManager authenticationManager){
        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    private RestUrlAuthFilter urlFilter(AuthenticationManager authenticationManager){
        RestUrlAuthFilter filter = new RestUrlAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
//        also can pass a secret/salt to better encode passwords. Has empty string by default
        return SfgPasswordEncoderFactory.createDelegatingPasswordEncoder();
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        return new BCryptPasswordEncoder();
    }

    String encodePwd(String pwd){
        return passwordEncoder().encode(pwd);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(headerFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();

        http.addFilterBefore(urlFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class);


        http
                .authorizeRequests(authorize ->
                    authorize
                            .antMatchers("/h2-console/**").permitAll() //do not use in production - unsafe
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

//        h-2 console configuration to allow access to in-browser h2-console
        http.headers().frameOptions().sameOrigin();
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
                .password(encodePwd("secret"))
                .roles("ADMIN")
                .and()
                .withUser("Derpy")
//                Added encoder configuration so there is no need to mention encoding algorithm in brackets
//                .password("{noop}muffinz")
//                .password(encodePwd("muffinz_power"))
                .password("{bcrypt}$2a$04$brgHBicSVHkjKG9BGA4atuLukGYGXsddXdbdSpD8T3OcjH0PaYX6m")
//                this encoded password used algo bcrypt with the power of 15
//                .password("{bcrypt15}$2a$15$K6.TqhK4ai2rBbZ3o.E6Qu1otad4HSUVye1J5dzi/0bxO8BB4UjsC")
                .roles("USER");
//        memory in-build customer
        auth.inMemoryAuthentication()
                .withUser("Fluttershy")
//        Added encoder configuration so there is no need to mention encoding algorithm in brackets. Don't use algorithm name in brackets if encoder is already configured
//                .password("{noop}wabbit")
                .password(encodePwd("wabbit"))
                .roles("CUSTOMER");
    }
}
