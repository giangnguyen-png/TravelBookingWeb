
package com.mycompany.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/activity")
public class AdminActivityController {

    @GetMapping
    public String list() {
        return "activity";
    }
}
