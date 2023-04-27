package com.ms.bap.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.bap.dao.UsersRepo;
import com.ms.bap.dto.JwtResponse;
import com.ms.bap.dto.LoginRequest;
import com.ms.bap.dto.UsersDTO;
import com.ms.bap.services.auth.UsersService;
import com.ms.bap.services.auth.UserDetailsImpl;
import com.ms.bap.util.ApplicationConstant;
import com.ms.bap.util.JsonUtil;
import com.ms.bap.util.JwtUtils;
import com.ms.bap.util.CommonUtil;
import com.ms.common.dto.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ApplicationConstant.CONTEXT_ROOT)
public class UsersController {

    @Autowired
    UsersService usersService;

    @Autowired
    JsonUtil jsonUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsersRepo usersRepo;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    ObjectMapper objectMapper;
    @PostMapping(path = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> users(@RequestBody String users, @RequestHeader HttpHeaders httpHeaders) throws JsonProcessingException {
        UsersDTO usersDTO = (UsersDTO) jsonUtil.toObject(users, UsersDTO.class);
        if (CommonUtil.isEmpty(usersDTO) || CommonUtil.isEmpty(usersDTO.getEmail()))
            return new ResponseEntity<>(objectMapper.writeValueAsString(new MessageResponse("Please provide valid mail id")), HttpStatus.BAD_REQUEST);
        if (CommonUtil.isEmpty(usersDTO) || CommonUtil.isEmpty(usersDTO.getMobile()))
            return new ResponseEntity<>(objectMapper.writeValueAsString(new MessageResponse("Please provide valid contact details")), HttpStatus.BAD_REQUEST);
        if (CommonUtil.isEmpty(usersDTO) || CommonUtil.isEmpty(usersDTO.getMobile()))
            return new ResponseEntity<>(objectMapper.writeValueAsString(new MessageResponse("Please provide valid contact details")), HttpStatus.BAD_REQUEST);
        if (CommonUtil.isEmpty(usersDTO) || CommonUtil.isEmpty(usersDTO.getPassword()))
            return new ResponseEntity<>(objectMapper.writeValueAsString(new MessageResponse("Password can not be null")), HttpStatus.BAD_REQUEST);
        if (CommonUtil.isEmpty(usersDTO) || CommonUtil.isEmpty(usersDTO.getFirstName()))
            return new ResponseEntity<>(objectMapper.writeValueAsString(new MessageResponse("Please provide First name")), HttpStatus.BAD_REQUEST);
        if (CommonUtil.isEmpty(usersDTO) || CommonUtil.isEmpty(usersDTO.getLastName()))
            return new ResponseEntity<>(objectMapper.writeValueAsString(new MessageResponse("Please provide Last name")), HttpStatus.BAD_REQUEST);
        if (usersRepo.existsByEmail(usersDTO.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(objectMapper.writeValueAsString(new MessageResponse("Error: Username is already taken!")));
        }
        if (usersRepo.existsByMobile(usersDTO.getMobile())) {
            return ResponseEntity
                    .badRequest()
                    .body(objectMapper.writeValueAsString(new MessageResponse("Error: Mobile is already in use by other user!")));
        }
        return new ResponseEntity<>(usersService.createUser(usersDTO), HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> signIn(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getEmail(),
                roles));
    }



}
