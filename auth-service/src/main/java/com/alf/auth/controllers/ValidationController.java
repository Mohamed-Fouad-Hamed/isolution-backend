package com.alf.auth.controllers;

import com.alf.auth.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ValidationController {

    private final UserService userService ;

    @GetMapping("/display-name")
    public ResponseEntity<Boolean> displayNameExists(@RequestParam("id") String id){
        boolean result = userService.loginIsExists(id);
        return ResponseEntity.ok(result);
    }

}