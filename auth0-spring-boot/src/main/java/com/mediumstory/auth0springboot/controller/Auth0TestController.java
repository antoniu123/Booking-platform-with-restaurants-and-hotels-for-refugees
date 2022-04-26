package com.mediumstory.auth0springboot.controller;

import com.mediumstory.auth0springboot.dto.ResponseDto;
import com.mediumstory.auth0springboot.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth0")
public class Auth0TestController {

    @Autowired
    private JwtService jwtService;

    @GetMapping(value = "/public")
    public ResponseEntity<ResponseDto> publicEndpoint() {
        return ResponseEntity.ok(new ResponseDto("Public Endpoint Working fine !"));
    }

    @GetMapping(value = "/private")
    public ResponseEntity<ResponseDto> privateEndpoint() {
        List<String> roles = jwtService.getRoles();
        System.out.println(jwtService.getUser());
        return ResponseEntity.ok(new ResponseDto("Private Endpoint Working fine !"));
    }

}