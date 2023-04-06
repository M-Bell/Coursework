package com.shyiko.coursework.dao;

import com.shyiko.coursework.model.Recipe;
import com.shyiko.coursework.model.UserProfile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
@Component
public class UserDao implements IUserDao {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private EntityTransaction transaction;

    @Override
    public void saveUser(UserProfile user) {
        try {
            // Begin transaction
            transaction.begin();

            // Save user to the database
            entityManager.persist(user);

            // Commit transaction
            transaction.commit();

            System.out.println("User saved successfully");
        } catch (Exception e) {
            // Rollback transaction if there is an error
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

    }

    @Override
    @Transactional
    public UserProfile getUserByUsername(String username) {
        TypedQuery<UserProfile> query = entityManager.createQuery(
                "SELECT u FROM UserProfile u WHERE u.username = :username", UserProfile.class);
        query.setParameter("username", username);
        UserProfile user = null;
        try {
            user = query.getSingleResult();
        } catch (NoResultException ignored) {
        }
        return user;
    }

    @Override
    public UserProfile getUserById(Long id) {
        return entityManager.find(UserProfile.class, id);
    }

    public void updateUser(UserProfile userProfile) {
        try {
            // Begin transaction
            transaction.begin();

            // Save user to the database
            entityManager.merge(userProfile);

            // Commit transaction
            transaction.commit();

            System.out.println("User saved successfully");
        } catch (Exception e) {
            // Rollback transaction if there is an error
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<UserProfile> getAllUsers() {
        TypedQuery<UserProfile> query = entityManager.createQuery(
                "SELECT u from UserProfile u", UserProfile.class);
        return query.getResultList();
    }
}
