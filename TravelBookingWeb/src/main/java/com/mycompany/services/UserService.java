/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.services;

import com.mycompany.pojo.Users;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 *
 * @author nguyen
 */
public interface UserService extends UserDetailsService{
    List<Users> getUsers();
    Users getUserById(Long id);
    Users getUserByUsername(String username);
    Users getUserByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Users addOrUpdateUser(Users user);
    void deleteUser(Long id);
}
