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
        String avatar = this.cloudinaryService.upload(user.getAvatarFile(), "travel/users");
        if (avatar != null) {
            user.setAvatar(avatar);
        }
        return this.userRepo.addOrUpdateUser(user);
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
