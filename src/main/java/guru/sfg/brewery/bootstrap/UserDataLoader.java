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
            Authority adminRole = authorityRepository.save(Authority.builder().role("ROLE_ADMIN").build());
            Authority userRole = authorityRepository.save(Authority.builder().role("ROLE_USER").build());
            Authority customerRole = authorityRepository.save(Authority.builder().role("ROLE_CUSTOMER").build());

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
            userRepository.save(User.builder()
                    .username("Derpy")
                    .password("{bcrypt}$2a$04$yb4zi/Yxdxn2YRPhqC0WzOv3bIe19VqGnuK8xq8YbPU7x1bQd7TaW")
                    .authority(adminRole)
                    .build()
            );
            userRepository.save(User.builder()
                    .username("Bonbon")
                    .password("{noop}secret")
                    .authority(customerRole)
                    .build()
            );
            userRepository.save(User.builder()
                    .username("Fluttershy")
                    .password("{noop}wabbit")
                    .authority(customerRole)
                    .build()
            );
        }
    }
}
