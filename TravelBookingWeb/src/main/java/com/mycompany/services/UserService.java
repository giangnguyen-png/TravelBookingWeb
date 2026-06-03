
package com.mycompany.services;

import com.mycompany.pojo.Users;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

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
