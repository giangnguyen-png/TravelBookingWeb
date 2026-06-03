
package com.mycompany.repositories.impl;

import com.mycompany.pojo.BusTrips;
import com.mycompany.repositories.BusTripRepository;
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
public class BusTripRepositoryImpl implements BusTripRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<BusTrips> searchBusTrips(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<BusTrips> q = b.createQuery(BusTrips.class);
        Root<BusTrips> root = q.from(BusTrips.class);
        q.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String departureLocationId = params.get("departureLocationId");
            if (departureLocationId != null && !departureLocationId.isBlank()) {
                predicates.add(b.equal(root.get("departureLocationId").get("id"), Long.valueOf(departureLocationId)));
            }

            String arrivalLocationId = params.get("arrivalLocationId");
            if (arrivalLocationId != null && !arrivalLocationId.isBlank()) {
                predicates.add(b.equal(root.get("arrivalLocationId").get("id"), Long.valueOf(arrivalLocationId)));
            }

            Date fromDate = RepositoryUtils.parseDate(params.get("fromDate"));
            if (fromDate != null) {
                predicates.add(b.greaterThanOrEqualTo(root.get("departureTime"), fromDate));
            }

            Date toDate = RepositoryUtils.parseDate(params.get("toDate"));
            if (toDate != null) {
                predicates.add(b.lessThanOrEqualTo(root.get("departureTime"), toDate));
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
        q.orderBy(b.asc(root.get("departureTime")));

        Query<BusTrips> query = session.createQuery(q);
        RepositoryUtils.paginate(query, params);
        return query.getResultList();
    }

    @Override
    public List<BusTrips> getBusTripsByProviderId(Long providerId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<BusTrips> query = session.createQuery(
                "FROM BusTrips b WHERE b.providerId.id = :providerId ORDER BY b.departureTime DESC", BusTrips.class);
        query.setParameter("providerId", providerId);
        return query.getResultList();
    }

    @Override
    public List<BusTrips> getBusTripsByIds(List<Long> ids) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<BusTrips> query = session.createQuery("FROM BusTrips b WHERE b.id IN (:ids)", BusTrips.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public BusTrips getBusTripById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(BusTrips.class, id);
    }

    @Override
    public BusTrips addOrUpdateBusTrip(BusTrips busTrip) {
        Session session = this.factory.getObject().getCurrentSession();
        if (busTrip.getId() == null) {
            session.persist(busTrip);
            return busTrip;
        }
        return session.merge(busTrip);
    }

    @Override
    public void deleteBusTrip(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        BusTrips busTrip = this.getBusTripById(id);
        if (busTrip != null) {
            session.remove(busTrip);
        }
    }
}
