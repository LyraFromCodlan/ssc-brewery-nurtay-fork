package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public void run(String... args) {
        if (authorityRepository.count()==0) {
            Authority adminRole = authorityRepository.save(Authority.builder().role("ADMIN").build());
            Authority userRole = authorityRepository.save(Authority.builder().role("USER").build());
            Authority customerRole = authorityRepository.save(Authority.builder().role("CUSTOMER").build());

            userRepository.save(
                    User.builder()
                            .username("User")
                            .password("{ldap}{SSHA}lA509HUqvZQgP6vKtPxjCptDNSr1/uTPKDDIGg==")
                            .authority(userRole)
                            .build()
            );
            userRepository.save(
                    User.builder()
                            .username("SPRING")
                            .password("{bcrypt}$2a$04$WrmP30YbV5B7cH8qVUTWc.khEgGJK.Ub/rRAsqqPRozjIx8SHd/rG")
                            .authority(adminRole)
                            .build()
            );
            userRepository.save(User.builder()
                    .username("Scott")
                    .password("{noop}SCOTT")
                    .authority(customerRole)
                    .build()
            );
        }
    }
}
