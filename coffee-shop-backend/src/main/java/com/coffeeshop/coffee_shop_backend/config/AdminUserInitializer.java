package com.coffeeshop.coffee_shop_backend.config;

import com.coffeeshop.coffee_shop_backend.model.StaffRole;
import com.coffeeshop.coffee_shop_backend.model.StaffUser;
import com.coffeeshop.coffee_shop_backend.repository.StaffUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    private final StaffUserRepository staffUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserInitializer(StaffUserRepository staffUserRepository, PasswordEncoder passwordEncoder) {
        this.staffUserRepository = staffUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (staffUserRepository.findByUsername("manager").isEmpty()) {

            System.out.println("No manager account found. Creating default manager...");

            StaffUser manager = new StaffUser();
            manager.setUsername("manager");
            manager.setHashedPassword(passwordEncoder.encode("QWERTY"));
            manager.setRole(StaffRole.ROLE_MANAGER);

            staffUserRepository.save(manager);

            System.out.println("Default manager account created with username 'manager' and password 'QWERTY'.");
            System.out.println("!!! PLEASE CHANGE THIS PASSWORD IMMEDIATELY !!!");
        }
    }
}