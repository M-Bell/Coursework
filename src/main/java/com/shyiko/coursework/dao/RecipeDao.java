package com.shyiko.coursework.dao;

import com.shyiko.coursework.model.Recipe;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
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
public class RecipeDao extends BaseDao {

    public void updateRating(Long recipeId) {
        try {
            transaction.begin();
            TypedQuery<Recipe> query = entityManager.createQuery(
                    "SELECT u from Recipe u WHERE u.id = :id", Recipe.class);
            query.setParameter("id", recipeId);
            Recipe recipe = query.getSingleResult();
            if (recipe == null) return;
            TypedQuery<Double> ratingQuery = entityManager.createQuery(
                    "SELECT AVG (u.rating) from UserRating u WHERE u.recipe.name = :name", Double.class);
            ratingQuery.setParameter("name", recipe.getName());
            Double averageRating = ratingQuery.getSingleResult();
            recipe.setRating(BigDecimal.valueOf(averageRating));
            entityManager.merge(recipe);
            transaction.commit();
        } catch (Exception e) {
            // Rollback transaction if there is an error
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

    }

    public List<Recipe> getRecipeByAuthor(String username) {
        TypedQuery<Recipe> query = entityManager.createQuery(
                "SELECT u from Recipe u WHERE u.author.username = :username", Recipe.class);
        query.setParameter("username", username);
        return query.getResultList();
    }

    public void deleteRecipe(Long id) {
        try {
            transaction.begin();
            Query query = entityManager.createQuery("DELETE FROM Recipe r WHERE r.id = :recipeId");
            query.setParameter("recipeId", id);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            // Rollback transaction if there is an error
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

    }
}
