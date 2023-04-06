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
public class UtilService implements IUtilService {
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

    @Override
    public List<String> getAllCategories() {
        return utilDao.getAllCategories().stream().map(Category::getName).collect(Collectors.toList());
    }

    @Override
    public List<String> getAllAllergies() {
        return utilDao.getAllAllergies().stream().map(Allergy::getName).collect(Collectors.toList());
    }

    @Override
    public Category getCategoryByName(String name) {
        return utilDao.getCategoryByName(name);
    }

    @Override
    public Allergy getAllergyByName(String name) {
        return utilDao.getAllergyByName(name);
    }

    public void rateRecipe(Long recipeId, Integer rating) {
        if (utilDao.userVoted(userService.getCurrentUser(), recipeId)) {
            utilDao.updateUserVotedRating(userService.getCurrentUser(), recipeId, rating);
        } else {
            recipeService.addVoted(recipeId);
            utilDao.saveRating(new UserRating(userService.getCurrentUser(), recipeService.getRecipeById(recipeId), BigDecimal.valueOf(rating)));
        }
        recipeService.updateRating(recipeId);
    }

    public List<Allergy> getAllergiesById(Long id) {
        List<RecipeAllergy> recipeAllergies = recipeAllergyDao.getRecipeAllergies(id);
        if (recipeAllergies == null) return new ArrayList<>();
        return recipeAllergies.stream().map(RecipeAllergy::getAllergy).toList();
    }

    public double getRating(Long id, UserProfile currentUser) {
        UserRating rating = ratingDao.getUserRating(id, currentUser.getId());
        return rating == null ? 0 : rating.getRating().doubleValue();
    }
}
