/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controllers;

import com.mycompany.enums.UserRole;
import com.mycompany.pojo.Users;
import com.mycompany.services.UserService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author nguyen
 */
@Controller
@RequestMapping("/admin")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginView() {
        return "login";
    }

    @GetMapping("/users")
    public String list(Model model) {
        model.addAttribute("users", this.userService.getUsers());
        model.addAttribute("user", new Users());
        model.addAttribute("roles", UserRole.values());
        return "users";
    }

    @GetMapping("/users/{id}")
    public String updateView(Model model, @PathVariable(value = "id") Long id) {
        model.addAttribute("users", this.userService.getUsers());
        model.addAttribute("user", this.userService.getUserById(id));
        model.addAttribute("roles", UserRole.values());
        return "users";
    }

    @PostMapping("/users")
    public String save(@ModelAttribute(value = "user") Users user) {
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(new Date());
        }
        this.userService.addOrUpdateUser(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String delete(@PathVariable(value = "id") Long id) {
        this.userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}
