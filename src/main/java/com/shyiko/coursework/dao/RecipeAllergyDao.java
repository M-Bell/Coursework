package com.shyiko.coursework.dao;


import com.shyiko.coursework.model.Allergy;
import com.shyiko.coursework.model.Recipe;
import com.shyiko.coursework.model.RecipeAllergy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
@Component
public class RecipeAllergyDao extends BaseDao {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private EntityTransaction transaction;

    public void removeExisting(Recipe recipe) {
        try {
            transaction.begin();
            Query query = entityManager.createQuery(
                    "DELETE FROM RecipeAllergy u WHERE u.recipe.id = :id");
            query.setParameter("id", recipe.getId());
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

    public List<RecipeAllergy> getRecipeAllergies(Long id) {
        TypedQuery<RecipeAllergy> query = entityManager.createQuery(
                "SELECT u FROM RecipeAllergy u WHERE u.id.recipeId= :id", RecipeAllergy.class);
        query.setParameter("id", id);
        return query.getResultList();
    }
}
