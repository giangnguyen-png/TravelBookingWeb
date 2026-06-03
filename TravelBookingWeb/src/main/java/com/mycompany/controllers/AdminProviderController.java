
package com.mycompany.controllers;

import com.mycompany.enums.VerificationStatus;
import com.mycompany.services.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/providers")
public class AdminProviderController {

    @Autowired
    private ProviderService providerService;

    @GetMapping
    public String list(Model model, @RequestParam(value = "status", required = false) String status) {
        VerificationStatus verificationStatus = status != null && !status.isBlank()
                ? VerificationStatus.valueOf(status)
                : VerificationStatus.PENDING;

        model.addAttribute("providers", this.providerService.getProvidersByStatus(verificationStatus));
        model.addAttribute("status", verificationStatus);
        return "providers";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable(value = "id") Long id, @RequestParam(value = "status") String status) {
        this.providerService.updateVerificationStatus(id, VerificationStatus.valueOf(status));
        return "redirect:/admin/providers";
    }
}
