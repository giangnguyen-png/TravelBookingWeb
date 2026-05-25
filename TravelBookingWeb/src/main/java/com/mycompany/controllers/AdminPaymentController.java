/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controllers;

import com.mycompany.enums.VerificationStatus;
import com.mycompany.services.PaymentService;
import com.mycompany.services.ProviderService;
import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author nguyen
 */
@Controller
@RequestMapping("/admin/payments")
public class AdminPaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ProviderService providerService;

    @GetMapping
    public String list(Model model,
            @RequestParam(value = "providerId", required = false) Long providerId,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate) {
        model.addAttribute("providers", this.providerService.getProvidersByStatus(VerificationStatus.APPROVED));
        model.addAttribute("providerId", providerId);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);

        if (providerId != null) {
            Date from = fromDate != null && !fromDate.isBlank() ? Date.valueOf(fromDate) : null;
            Date to = toDate != null && !toDate.isBlank() ? Date.valueOf(toDate) : null;
            model.addAttribute("revenue", this.paymentService.sumRevenueByProvider(providerId, from, to));
        }

        return "payments";
    }
}
