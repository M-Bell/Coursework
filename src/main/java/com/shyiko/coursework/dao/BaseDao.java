package com.shyiko.coursework.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
@Component
public abstract class BaseDao {
    @Autowired
    protected EntityManager entityManager;
    @Autowired
    protected EntityTransaction transaction;

    public void save(Object entity) {
        try {
            // Begin transaction
            transaction.begin();
            // Save entity to the database
            entityManager.persist(entity);
            // Commit transaction
            transaction.commit();
            System.out.println("Entity saved successfully");
        } catch (Exception e) {
            // Rollback transaction if there is an error
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void update(Object entity) {
        try {
            transaction.begin();
            entityManager.merge(entity);
            transaction.commit();
        } catch (Exception e) {
            // Rollback transaction if there is an error
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Object getByKey(Class entity, Object key) {
        return entityManager.find(entity, key);
    }


    public List getAll(Class entity) {
        Query query = entityManager.createQuery(
                "SELECT u from " + entity.getSimpleName() + " u", entity);
        return query.getResultList();
    }
}
