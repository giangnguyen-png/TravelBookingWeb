/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.services.impl;

import com.mycompany.pojo.Users;
import com.mycompany.repositories.UserRepository;
import com.mycompany.services.CloudinaryService;
import com.mycompany.services.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author nguyen
 */
@Service("userDetailService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Users> getUsers() {
        return this.userRepo.getUsers();
    }

    @Override
    public Users getUserById(Long id) {
        return this.userRepo.getUserById(id);
    }

    @Override
    public Users getUserByUsername(String username) {
        return this.userRepo.getUserByUsername(username);
    }

    @Override
    public Users getUserByEmail(String email) {
        return this.userRepo.getUserByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return this.userRepo.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.userRepo.existsByEmail(email);
    }

    @Override
    public Users addOrUpdateUser(Users user) {
        if (user.getId() != null && (user.getPassword() == null || user.getPassword().isBlank())) {
            Users currentUser = this.userRepo.getUserById(user.getId());
            if (currentUser != null) {
                user.setPassword(currentUser.getPassword());
            }
        } else if (user.getPassword() != null) {
            if (isEncodedPassword(user.getPassword())) {
                user.setPassword(normalizeStoredPassword(user.getPassword()));
            } else {
                user.setPassword(this.passwordEncoder.encode(user.getPassword()));
            }
        }

        String avatar = this.cloudinaryService.upload(user.getAvatarFile(), "travel/users");
        if (avatar != null) {
            user.setAvatar(avatar);
        }
        return this.userRepo.addOrUpdateUser(user);
    }

    private boolean isEncodedPassword(String password) {
        String normalizedPassword = normalizeStoredPassword(password);
        return normalizedPassword.startsWith("$2a$") || normalizedPassword.startsWith("$2b$") || normalizedPassword.startsWith("$2y$");
    }

    private String normalizeStoredPassword(String password) {
        String normalizedPassword = password.trim();
        while (normalizedPassword.length() >= 2
                && ((normalizedPassword.startsWith("'") && normalizedPassword.endsWith("'"))
                || (normalizedPassword.startsWith("\"") && normalizedPassword.endsWith("\"")))) {
            normalizedPassword = normalizedPassword.substring(1, normalizedPassword.length() - 1).trim();
        }
        return normalizedPassword;
    }

    @Override
    public void deleteUser(Long id) {
        this.userRepo.deleteUser(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users u = this.getUserByUsername(username);
        if (u == null) {
            throw new UsernameNotFoundException("Invalid username!");
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + u.getRole().name()));
        return new org.springframework.security.core.userdetails.User(u.getUsername(),
                u.getPassword(), authorities);
    }
}
