package com.BugTrackingPortal.bugbuster.dto;


import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Set<String> roles; // e.g., ["ADMIN"], ["TESTER"]
}

