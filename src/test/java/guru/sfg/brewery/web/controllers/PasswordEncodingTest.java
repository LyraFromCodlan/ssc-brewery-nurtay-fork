package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class PasswordEncodingTest {
    static final String PASSWORD = "muffinz_power";

    @Test
    void testBCrypt() {
//        Has adjustable strength. Best is 6 for user and 8 for superuser
        PasswordEncoder bCrypt = new BCryptPasswordEncoder(4);
        System.out.println(bCrypt.encode(PASSWORD));
        System.out.println(bCrypt.encode(PASSWORD));
    }

    @Test
    void testSha256() {
        PasswordEncoder sha256 = new StandardPasswordEncoder();
        System.out.println(sha256.encode(PASSWORD));
        System.out.println(sha256.encode(PASSWORD));
    }

    @Test
    void testLdap() {
        PasswordEncoder ldap = new LdapShaPasswordEncoder();
        System.out.println(ldap.encode(PASSWORD));
        System.out.println(ldap.encode(PASSWORD));

//        Comparison by reference value nor through equals doesn't work
        System.out.println(ldap.encode(PASSWORD).equals(ldap.encode(PASSWORD)));
//        Only use encoder's matcher when comparing password and its hash value
        assertTrue(ldap.matches(PASSWORD, ldap.encode(PASSWORD)));
    }

    @Test
    void testNoOp() {
        PasswordEncoder noOp = NoOpPasswordEncoder.getInstance();
        System.out.println(noOp.encode(PASSWORD));
    }

    @Test
    void hashingExample(){
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));
        System.out.println(DigestUtils.md5DigestAsHex(PASSWORD.getBytes()));

        String salt = PASSWORD + "my_SalTed_va1ue";
        System.out.println(DigestUtils.md5DigestAsHex(salt.getBytes()));

    }
}
