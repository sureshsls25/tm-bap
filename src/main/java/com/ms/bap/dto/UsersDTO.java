package com.ms.bap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UsersDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private String mobile;

    @JsonProperty("address")
    private AddressDto addressDto;
    private String type;
}
