package com.jwtvalidator.controller;

import com.jwtvalidator.exception.JwtValidationException;
import com.jwtvalidator.service.JwtValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class JwtValidatorController {

    @Autowired
    private JwtValidatorService jwtValidatorService;

    @PostMapping("/validate")
    public boolean validateJwt(@RequestBody String jwt) {
        try {
            return jwtValidatorService.validate(jwt);
        } catch (JwtValidationException ex) {
            return false;
        }
    }
}
