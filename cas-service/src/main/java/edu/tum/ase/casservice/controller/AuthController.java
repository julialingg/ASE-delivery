package edu.tum.ase.casservice.controller;


import edu.tum.ase.casservice.model.AuthRequest;
import edu.tum.ase.casservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

// accept authentication request
@RestController
@RequestMapping("/auth")

//@CrossOrigin(origins = "*", allowedHeaders = "*")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*",allowCredentials = "true")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping
    // TODO: Implement Authentication of the user credentials
    // @RequestHeader: retrieve the value of any non-readonly header in the HTTP request
    public ResponseEntity<String> login(@RequestHeader("Authorization") String authorization, HttpServletRequest request, HttpServletResponse response) throws Exception{

        System.out.println("login function");
        return authService.authenticateUser(authorization,request,response);
    }

    // just done by springboot !!
    @GetMapping("/csrf")
    public String csrf() throws Exception{
        return "client call this function to get your csrf token";

    }

    @PostMapping("/jwe")
    public ResponseEntity<?> loginJwe(@RequestBody AuthRequest loginRequest) throws Exception {
        System.out.println("JWE login function");
        return authService.createAuthToken(loginRequest);
    }

    //  frontend call this to get public key
    @GetMapping("/pkey")
    public ResponseEntity<HashMap<String, String>> getPublicKey() throws Exception {
        HashMap<String, String> pKeyData = authService.getPublicKeyData();
        return new ResponseEntity<HashMap<String, String>>(pKeyData, HttpStatus.OK);
    }

}