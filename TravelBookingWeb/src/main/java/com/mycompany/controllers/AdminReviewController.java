
package com.mycompany.controllers;

import com.mycompany.enums.VerificationStatus;
import com.mycompany.services.ProviderService;
import com.mycompany.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/reviews")
public class AdminReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ProviderService providerService;

    @GetMapping
    public String list(Model model, @RequestParam(value = "providerId", required = false) Long providerId) {
        model.addAttribute("providers", this.providerService.getProvidersByStatus(VerificationStatus.APPROVED));
        model.addAttribute("providerId", providerId);
        if (providerId != null) {
            model.addAttribute("reviewsData", this.reviewService.getProviderReviewSummary(providerId));
        }
        return "reviews";
    }
}
