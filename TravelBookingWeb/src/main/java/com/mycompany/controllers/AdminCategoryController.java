/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controllers;

import com.mycompany.enums.BusinessType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author nguyen
 */
@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", BusinessType.values());
        return "categories";
    }
}
