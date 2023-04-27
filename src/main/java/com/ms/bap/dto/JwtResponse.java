package com.ms.bap.dto;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {

    private String jwt;
    private String email;
    private List<String> role;

    public JwtResponse(String jwt, String email, List<String> roles) {
        this.jwt = jwt;
        this.email = email;
        this.role = roles;
    }
}
