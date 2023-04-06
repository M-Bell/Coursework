package com.shyiko.coursework.dao;

import com.shyiko.coursework.model.Allergy;
import com.shyiko.coursework.model.Category;
import com.shyiko.coursework.model.UserProfile;
import com.shyiko.coursework.model.UserRating;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@Transactional
@Component
public class UtilDao implements IUtilDao {

    @Autowired
    private EntityTransaction transaction;
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Category> getAllCategories() {
        TypedQuery<Category> query = entityManager.createQuery(
                "SELECT u from Category u", Category.class);

        return query.getResultList();
    }

    @Override
    public List<Allergy> getAllAllergies() {
        TypedQuery<Allergy> query = entityManager.createQuery(
                "SELECT u from Allergy u", Allergy.class);

        return query.getResultList();
    }

    public Category getCategoryByName(String name) {
        TypedQuery<Category> query = entityManager.createQuery(
                "SELECT u from Category u WHERE u.name = :name", Category.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

    @Override
    public Allergy getAllergyByName(String name) {
        TypedQuery<Allergy> query = entityManager.createQuery(
                "SELECT u from Allergy u WHERE u.name = :name", Allergy.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

    public void saveRating(UserRating userRating) {
        try {
            // Begin transaction
            transaction.begin();

            // Save user to the database
            entityManager.persist(userRating);

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

    public boolean userVoted(UserProfile currentUser, Long id) {
        TypedQuery<UserRating> query = entityManager.createQuery(
                "SELECT u from UserRating u WHERE u.user.username = :name", UserRating.class);
        query.setParameter("name", currentUser.getUsername());
        return query.getResultList().stream().map(x -> x.getRecipe().getId()).toList().contains(id);
    }

    public void updateUserVotedRating(UserProfile currentUser, Long recipeId, Integer rating) {
        TypedQuery<UserRating> query = entityManager.createQuery(
                "SELECT u from UserRating u WHERE u.user.username = :name AND u.recipe.id = :recipeId",
                UserRating.class);
        query.setParameter("name", currentUser.getUsername());
        query.setParameter("recipeId", recipeId);
        UserRating userRating = query.getSingleResult();
        if (userRating == null) return;
        userRating.setRating(BigDecimal.valueOf(rating));
        entityManager.merge(userRating);
    }
}
