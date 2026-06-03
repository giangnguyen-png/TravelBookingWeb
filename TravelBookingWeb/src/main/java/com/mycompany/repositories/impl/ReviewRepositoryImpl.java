
package com.mycompany.repositories.impl;

import com.mycompany.pojo.Reviews;
import com.mycompany.repositories.ReviewRepository;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ReviewRepositoryImpl implements ReviewRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Reviews addReview(Reviews review) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(review);
        return review;
    }

    @Override
    public List<Reviews> getReviewsByProviderId(Long providerId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Reviews> query = session.createQuery(
                "FROM Reviews r WHERE r.providerId.id = :providerId ORDER BY r.createdAt DESC", Reviews.class);
        query.setParameter("providerId", providerId);
        return query.getResultList();
    }

    @Override
    public Double getAverageRatingByProviderId(Long providerId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Double> query = session.createQuery(
                "SELECT AVG(r.rating) FROM Reviews r WHERE r.providerId.id = :providerId", Double.class);
        query.setParameter("providerId", providerId);
        return query.uniqueResult();
    }

    @Override
    public Long countReviewsByProviderId(Long providerId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Long> query = session.createQuery(
                "SELECT COUNT(r.id) FROM Reviews r WHERE r.providerId.id = :providerId", Long.class);
        query.setParameter("providerId", providerId);
        return query.uniqueResult();
    }
}
