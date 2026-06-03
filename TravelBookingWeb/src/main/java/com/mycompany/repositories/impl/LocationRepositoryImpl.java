
package com.mycompany.repositories.impl;

import com.mycompany.pojo.Locations;
import com.mycompany.repositories.LocationRepository;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class LocationRepositoryImpl implements LocationRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Locations> getLocations() {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Locations> query = session.createQuery("FROM Locations l ORDER BY l.province", Locations.class);
        return query.getResultList();
    }

    @Override
    public List<Locations> searchLocations(String keyword) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Locations> query = session.createQuery(
                "FROM Locations l WHERE l.province LIKE :kw OR l.country LIKE :kw ORDER BY l.province",
                Locations.class);
        query.setParameter("kw", String.format("%%%s%%", keyword));
        return query.getResultList();
    }

    @Override
    public Locations getLocationById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Locations.class, id);
    }
}
