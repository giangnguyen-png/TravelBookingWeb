/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.repositories;

import com.mycompany.enums.VerificationStatus;
import com.mycompany.pojo.ProviderProfiles;
import java.util.List;

/**
 *
 * @author nguyen
 */
public interface ProviderRepository {
    ProviderProfiles getProviderById(Long id);
    ProviderProfiles getProviderByUserId(Long userId);
    List<ProviderProfiles> getProvidersByStatus(VerificationStatus status);
    ProviderProfiles addOrUpdateProvider(ProviderProfiles provider);
    void updateVerificationStatus(Long providerId, VerificationStatus status);
}
