package com.BugTrackingPortal.bugbuster.config;

import com.BugTrackingPortal.bugbuster.model.Role;
import com.BugTrackingPortal.bugbuster.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void initRoles() {
        createRoleIfNotExist("ADMIN");
        createRoleIfNotExist("DEVELOPER");
        createRoleIfNotExist("TESTER");
    }

    private void createRoleIfNotExist(String roleName) {
        roleRepository.findByName(roleName).orElseGet(() -> {
            Role role = new Role();
            role.setName(roleName);
            return roleRepository.save(role);
        });
    }
}

