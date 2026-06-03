
package com.mycompany.controllers;

import com.mycompany.enums.BusinessType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", BusinessType.values());
        return "categories";
    }
}
