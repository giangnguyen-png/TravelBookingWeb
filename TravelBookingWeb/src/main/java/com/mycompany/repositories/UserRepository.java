
package com.mycompany.repositories;

import com.mycompany.pojo.Users;
import java.util.List;


public interface UserRepository {
    List<Users> getUsers();
    Users getUserById(Long id);
    Users getUserByUsername(String username);
    Users getUserByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Users addOrUpdateUser(Users user);
    void deleteUser(Long id);
}
