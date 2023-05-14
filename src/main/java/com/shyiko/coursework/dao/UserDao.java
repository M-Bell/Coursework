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
public class UserDao extends BaseDao {

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

}
