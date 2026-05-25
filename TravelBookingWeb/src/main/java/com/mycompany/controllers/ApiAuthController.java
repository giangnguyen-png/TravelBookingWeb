/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controllers;

import com.mycompany.enums.BusinessType;
import com.mycompany.enums.UserRole;
import com.mycompany.enums.VerificationStatus;
import com.mycompany.pojo.ProviderProfiles;
import com.mycompany.pojo.Users;
import com.mycompany.services.ProviderService;
import com.mycompany.services.UserService;
import com.mycompany.utils.JwtUtils;
import java.security.Principal;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nguyen
 */
@RestController
@RequestMapping("/api")
@CrossOrigin
public class ApiAuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProviderService providerService;

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String email = params.get("email");

        if (this.userService.existsByUsername(username) || this.userService.existsByEmail(email)) {
            return new ResponseEntity<>("Username or email already exists", HttpStatus.BAD_REQUEST);
        }

        Users user = new Users();
        user.setFullName(params.get("fullName"));
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(params.get("password"));
        user.setAvatar(params.get("avatar"));
        user.setRole(UserRole.valueOf(params.getOrDefault("role", UserRole.CUSTOMER.name())));
        user.setCreatedAt(new Date());

        Users savedUser = this.userService.addOrUpdateUser(user);

        if (savedUser.getRole() == UserRole.PROVIDER) {
            ProviderProfiles provider = new ProviderProfiles();
            provider.setUserId(savedUser);
            provider.setCompanyName(params.get("companyName"));
            provider.setBusinessType(BusinessType.valueOf(params.get("businessType")));
            provider.setVerificationStatus(VerificationStatus.PENDING);
            provider.setCreatedAt(new Date());
            this.providerService.addOrUpdateProvider(provider);
        }

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Users user) throws Exception {
        Users u = this.userService.getUserByUsername(user.getUsername());
        if (u != null && u.getPassword().equals(user.getPassword())) {
            return new ResponseEntity<>(Map.of(
                    "token", JwtUtils.generateToken(u.getUsername()),
                    "user", u
            ), HttpStatus.OK);
        }

        return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/users/me")
    public ResponseEntity<Users> profile(Principal principal) {
        return new ResponseEntity<>(this.userService.getUserByUsername(principal.getName()), HttpStatus.OK);
    }

    @PutMapping("/users/me")
    public ResponseEntity<Users> updateProfile(@RequestBody Users user, Principal principal) {
        Users currentUser = this.userService.getUserByUsername(principal.getName());
        currentUser.setFullName(user.getFullName());
        currentUser.setEmail(user.getEmail());
        currentUser.setAvatar(user.getAvatar());

        return new ResponseEntity<>(this.userService.addOrUpdateUser(currentUser), HttpStatus.OK);
    }
}
