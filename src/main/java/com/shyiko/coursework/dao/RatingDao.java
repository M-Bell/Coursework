package com.shyiko.coursework.dao;

import com.shyiko.coursework.model.UserRating;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
@Component
public class RatingDao {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private EntityTransaction transaction;


    public UserRating getUserRating(Long recipe_id, Long user_id) {
        TypedQuery<UserRating> query =
                entityManager.createQuery(
                        "SELECT u FROM UserRating u " +
                                "WHERE u.id.recipeId = :recipe_id AND u.id.userId = :user_id", UserRating.class);
        query.setParameter("recipe_id", recipe_id);
        query.setParameter("user_id", user_id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
