package com.ms.bap.services.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.bap.dao.UsersRepo;
import com.ms.common.dto.*;
import com.ms.bap.dto.UsersDTO;
import com.ms.bap.entity.Address;
import com.ms.bap.entity.UserRoles;
import com.ms.bap.entity.Users;
import com.ms.bap.services.auth.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UsersService {

    @Autowired
    UsersRepo usersRepo;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    ObjectMapper objectMapper;


    public String createUser(UsersDTO usersDTO) throws JsonProcessingException {
        usersRepo.save(getUsers(usersDTO));
        return objectMapper.writeValueAsString(new MessageResponse("User registered successfully"));

    }

    private Users getUsers(UsersDTO usersDTO) {
        Users users = new Users();
        users.setFirstName(usersDTO.getFirstName());
        users.setLastName(usersDTO.getLastName());
        users.setEmail(usersDTO.getEmail());
        users.setMobile(usersDTO.getMobile());
        users.setPassword(encoder.encode(usersDTO.getPassword()));
        users.setRole(UserRoles.USER);
        Address address = new Address();
        address.setAddressLine1(usersDTO.getAddressDto().getAddressLine1());
        address.setAddressLine2(usersDTO.getAddressDto().getAddressLine2());
        address.setCity(usersDTO.getAddressDto().getCity());
        address.setState(usersDTO.getAddressDto().getState());
        address.setCountry(usersDTO.getAddressDto().getCountry());
        address.setZipCode(usersDTO.getAddressDto().getZipCode());
        users.setAddress(address);
        return users;
    }


    private UserDetailsImpl getCurrentUserDetails() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


}
