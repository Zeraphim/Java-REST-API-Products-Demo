package com.g4.RestApiProductsDemo.controller;

import com.g4.RestApiProductsDemo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public String createToken(@RequestParam String username, @RequestParam String password) {
        return jwtUtil.generateToken(username);
    }
}