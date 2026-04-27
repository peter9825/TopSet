
package com.peter.topset.Users;
import com.peter.topset.Users.Entity.RoleEntity;
import com.peter.topset.Users.Entity.UserEntity;
import com.peter.topset.Users.Repository.RoleRepository;
import com.peter.topset.Users.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class SeedAdmin implements CommandLineRunner {

    @Autowired
    private UserRepository repository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Value("${admin.password}")
    private String adminPassword;

    public void run(String... args) throws Exception {

        if (repository.findByEmail("admin@topset.com").isEmpty()) {

            UserEntity admin = new UserEntity();

            admin.setEmail("admin@topset.com");
            admin.setUsername("app_admin");
            admin.setPasswordHash(encoder.encode(adminPassword));

            RoleEntity role = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("admin role not found."));

            admin.setRoles(Collections.singletonList(role));
            repository.save(admin);


        }

    }
}


