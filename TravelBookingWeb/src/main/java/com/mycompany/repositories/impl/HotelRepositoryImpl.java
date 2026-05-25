/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.repositories.impl;

import com.mycompany.pojo.HotelRooms;
import com.mycompany.pojo.Hotels;
import com.mycompany.repositories.HotelRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
public class HotelRepositoryImpl implements HotelRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Hotels> searchHotels(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Hotels> q = b.createQuery(Hotels.class);
        Root<Hotels> root = q.from(Hotels.class);
        q.select(root).distinct(true);

        List<Predicate> predicates = new ArrayList<>();
        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isBlank()) {
                predicates.add(b.or(
                        b.like(root.get("hotelName"), String.format("%%%s%%", kw)),
                        b.like(root.get("address"), String.format("%%%s%%", kw))));
            }

            String locationId = params.get("locationId");
            if (locationId != null && !locationId.isBlank()) {
                predicates.add(b.equal(root.get("locationId").get("id"), Long.valueOf(locationId)));
            }

            String minPrice = params.get("minPrice");
            String maxPrice = params.get("maxPrice");
            if ((minPrice != null && !minPrice.isBlank()) || (maxPrice != null && !maxPrice.isBlank())) {
                Join<Hotels, HotelRooms> rooms = root.join("hotelRoomsSet", JoinType.INNER);
                if (minPrice != null && !minPrice.isBlank()) {
                    predicates.add(b.greaterThanOrEqualTo(rooms.get("pricePerNight"), new BigDecimal(minPrice)));
                }
                if (maxPrice != null && !maxPrice.isBlank()) {
                    predicates.add(b.lessThanOrEqualTo(rooms.get("pricePerNight"), new BigDecimal(maxPrice)));
                }
            }
        }

        q.where(predicates.toArray(Predicate[]::new));
        q.orderBy(b.desc(root.get("id")));

        Query<Hotels> query = session.createQuery(q);
        RepositoryUtils.paginate(query, params);
        return query.getResultList();
    }

    @Override
    public List<Hotels> getHotelsByProviderId(Long providerId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Hotels> query = session.createQuery(
                "FROM Hotels h WHERE h.providerId.id = :providerId ORDER BY h.id DESC", Hotels.class);
        query.setParameter("providerId", providerId);
        return query.getResultList();
    }

    @Override
    public List<Hotels> getHotelsByIds(List<Long> ids) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Hotels> query = session.createQuery("FROM Hotels h WHERE h.id IN (:ids)", Hotels.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public Hotels getHotelById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Hotels.class, id);
    }

    @Override
    public Hotels addOrUpdateHotel(Hotels hotel) {
        Session session = this.factory.getObject().getCurrentSession();
        if (hotel.getId() == null) {
            session.persist(hotel);
            return hotel;
        }
        return session.merge(hotel);
    }

    @Override
    public void deleteHotel(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        Hotels hotel = this.getHotelById(id);
        if (hotel != null) {
            session.remove(hotel);
        }
    }
}
