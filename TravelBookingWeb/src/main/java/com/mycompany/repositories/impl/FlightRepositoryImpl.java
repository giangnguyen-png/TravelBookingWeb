
package com.mycompany.repositories.impl;

import com.mycompany.pojo.Flights;
import com.mycompany.repositories.FlightRepository;
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
public class FlightRepositoryImpl implements FlightRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Flights> searchFlights(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Flights> q = b.createQuery(Flights.class);
        Root<Flights> root = q.from(Flights.class);
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

        Query<Flights> query = session.createQuery(q);
        RepositoryUtils.paginate(query, params);
        return query.getResultList();
    }

    @Override
    public List<Flights> getFlightsByProviderId(Long providerId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Flights> query = session.createQuery(
                "FROM Flights f WHERE f.providerId.id = :providerId ORDER BY f.departureTime DESC", Flights.class);
        query.setParameter("providerId", providerId);
        return query.getResultList();
    }

    @Override
    public List<Flights> getFlightsByIds(List<Long> ids) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Flights> query = session.createQuery("FROM Flights f WHERE f.id IN (:ids)", Flights.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public Flights getFlightById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Flights.class, id);
    }

    @Override
    public Flights addOrUpdateFlight(Flights flight) {
        Session session = this.factory.getObject().getCurrentSession();
        if (flight.getId() == null) {
            session.persist(flight);
            return flight;
        }
        return session.merge(flight);
    }

    @Override
    public void deleteFlight(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        Flights flight = this.getFlightById(id);
        if (flight != null) {
            session.remove(flight);
        }
    }
}
