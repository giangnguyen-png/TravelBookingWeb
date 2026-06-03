
package com.mycompany.repositories.impl;

import com.mycompany.pojo.Tours;
import com.mycompany.repositories.TourRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class TourRepositoryImpl implements TourRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Tours> searchTours(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Tours> q = b.createQuery(Tours.class);
        Root<Tours> root = q.from(Tours.class);
        q.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                predicates.add(b.like(root.get("title"), String.format("%%%s%%", kw)));
            }

            String departureLocationId = params.get("departureLocationId");
            if (departureLocationId != null && !departureLocationId.isBlank()) {
                predicates.add(b.equal(root.get("departureLocationId").get("id"), Long.valueOf(departureLocationId)));
            }

            String destinationLocationId = params.get("destinationLocationId");
            if (destinationLocationId != null && !destinationLocationId.isBlank()) {
                predicates.add(b.equal(root.get("destinationLocationId").get("id"), Long.valueOf(destinationLocationId)));
            }

            Date fromDate = RepositoryUtils.parseDate(params.get("fromDate"));
            if (fromDate != null) {
                predicates.add(b.greaterThanOrEqualTo(root.get("departureDate"), fromDate));
            }

            Date toDate = RepositoryUtils.parseDate(params.get("toDate"));
            if (toDate != null) {
                predicates.add(b.lessThanOrEqualTo(root.get("departureDate"), toDate));
            }

            String minPrice = params.get("minPrice");
            if (minPrice != null && !minPrice.isBlank()) {
                predicates.add(b.greaterThanOrEqualTo(root.get("price"), new BigDecimal(minPrice)));
            }

            String maxPrice = params.get("maxPrice");
            if (maxPrice != null && !maxPrice.isBlank()) {
                predicates.add(b.lessThanOrEqualTo(root.get("price"), new BigDecimal(maxPrice)));
            }
        }

        q.where(predicates.toArray(Predicate[]::new));
        q.orderBy(b.desc(root.get("id")));

        Query<Tours> query = session.createQuery(q);
        RepositoryUtils.paginate(query, params);
        return query.getResultList();
    }

    @Override
    public List<Tours> getToursByProviderId(Long providerId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Tours> query = session.createQuery(
                "FROM Tours t WHERE t.providerId.id = :providerId ORDER BY t.id DESC", Tours.class);
        query.setParameter("providerId", providerId);
        return query.getResultList();
    }

    @Override
    public List<Tours> getToursByIds(List<Long> ids) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Tours> query = session.createQuery("FROM Tours t WHERE t.id IN (:ids)", Tours.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public Tours getTourById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Tours.class, id);
    }

    @Override
    public Tours addOrUpdateTour(Tours tour) {
        Session session = this.factory.getObject().getCurrentSession();
        if (tour.getId() == null) {
            session.persist(tour);
            return tour;
        }
        return session.merge(tour);
    }

    @Override
    public void deleteTour(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        Tours tour = this.getTourById(id);
        if (tour != null) {
            session.remove(tour);
        }
    }
}
