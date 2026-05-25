/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.repositories.impl;

import com.mycompany.pojo.Users;
import com.mycompany.repositories.UserRepository;
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
public class UserRepositoryImpl implements UserRepository{

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Users> getUsers() {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Users> query = session.createQuery("FROM Users", Users.class);
        return query.getResultList();
    }

    @Override
    public Users getUserById(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Users.class, id);
    }

    @Override
    public Users getUserByUsername(String username) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Users> query = session.createNamedQuery("Users.findByUsername", Users.class);
        query.setParameter("username", username);
        return query.uniqueResult();
    }

    @Override
    public Users getUserByEmail(String email) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Users> query = session.createNamedQuery("Users.findByEmail", Users.class);
        query.setParameter("email", email);
        return query.uniqueResult();
    }

    @Override
    public boolean existsByUsername(String username) {
        return this.getUserByUsername(username) != null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.getUserByEmail(email) != null;
    }

    @Override
    public Users addOrUpdateUser(Users user) {
        Session session = this.factory.getObject().getCurrentSession();
        if (user.getId() == null) {
            session.persist(user);
            return user;
        }
        return session.merge(user);
    }

    @Override
    public void deleteUser(Long id) {
        Session session = this.factory.getObject().getCurrentSession();
        Users user = session.get(Users.class, id);
        if (user != null) {
            session.remove(user);
        }
    }
    
}
