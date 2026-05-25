/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.repositories.impl;

import com.mycompany.enums.VerificationStatus;
import com.mycompany.pojo.ProviderProfiles;
import com.mycompany.repositories.ProviderRepository;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author nguyen
 */
@Repository
@Transactional
public class ProviderRepositoryImpl implements ProviderRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public ProviderProfiles getProviderById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(ProviderProfiles.class, id);
    }

    @Override
    public ProviderProfiles getProviderByUserId(Long userId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<ProviderProfiles> query = session.createQuery(
                "FROM ProviderProfiles p WHERE p.userId.id = :userId", ProviderProfiles.class);
        query.setParameter("userId", userId);
        return query.uniqueResult();
    }

    @Override
    public List<ProviderProfiles> getProvidersByStatus(VerificationStatus status) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<ProviderProfiles> query = session.createNamedQuery(
                "ProviderProfiles.findByVerificationStatus", ProviderProfiles.class);
        query.setParameter("verificationStatus", status);
        return query.getResultList();
    }

    @Override
    public ProviderProfiles addOrUpdateProvider(ProviderProfiles provider) {
        Session session = this.factory.getObject().getCurrentSession();
        if (provider.getId() == null) {
            session.persist(provider);
            return provider;
        }
        return session.merge(provider);
    }

    @Override
    public void updateVerificationStatus(Long providerId, VerificationStatus status) {
        Session session = this.factory.getObject().getCurrentSession();
        Query query = session.createQuery(
                "UPDATE ProviderProfiles p SET p.verificationStatus = :status WHERE p.id = :id");
        query.setParameter("status", status);
        query.setParameter("id", providerId);
        query.executeUpdate();
    }
}
