package com.shyiko.coursework.service;

import com.shyiko.coursework.dao.RatingDao;
import com.shyiko.coursework.dao.RecipeAllergyDao;
import com.shyiko.coursework.dao.UtilDao;
import com.shyiko.coursework.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UtilService {
    @Autowired
    private UtilDao utilDao;

    @Autowired
    private RatingDao ratingDao;

    @Autowired
    private UserService userService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeAllergyDao recipeAllergyDao;

    public List<String> getAllCategories() {
        return ((List<Category>) utilDao.getAll(Category.class)).stream().map(Category::getName).collect(Collectors.toList());
    }

    public List<String> getAllAllergies() {
        return ((List<Allergy>) utilDao.getAll(Allergy.class)).stream().map(Allergy::getName).collect(Collectors.toList());
    }

    public Category getCategoryByName(String name) {
        return (Category) utilDao.getByKey(Category.class, name);
    }

    public Allergy getAllergyByName(String name) {
        return (Allergy) utilDao.getByKey(Allergy.class, name);
    }

    public void rateRecipe(Long recipeId, Integer rating) {
        UserRating oldRating = (UserRating) ratingDao.getByKey(
                UserRating.class,
                new UserRating.UserRatingId(userService.getCurrentUser().getId(), recipeId));
        if (oldRating!=null) {
            oldRating.setRating(BigDecimal.valueOf(rating));
            ratingDao.update(oldRating);
        } else {
            recipeService.addVoted(recipeId);
            ratingDao.save(new UserRating(userService.getCurrentUser(), recipeService.getRecipeById(recipeId), BigDecimal.valueOf(rating)));
        }
        recipeService.updateRating(recipeId);
    }

    public List<Allergy> getAllergiesById(Long id) {
        List<RecipeAllergy> recipeAllergies = recipeAllergyDao.getRecipeAllergies(id);
        if (recipeAllergies == null) return new ArrayList<>();
        return recipeAllergies.stream().map(RecipeAllergy::getAllergy).toList();
    }

    public double getRating(Long id, UserProfile currentUser) {
        UserRating rating = (UserRating) ratingDao.getByKey(UserRating.class, new UserRating.UserRatingId(currentUser.getId(), id));
        return rating == null ? 0 : rating.getRating().doubleValue();
    }
}
