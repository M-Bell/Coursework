package com.shyiko.coursework.service;

import com.shyiko.coursework.dao.RecipeAllergyDao;
import com.shyiko.coursework.dao.RecipeDao;
import com.shyiko.coursework.dao.UtilDao;
import com.shyiko.coursework.model.Allergy;
import com.shyiko.coursework.model.Recipe;
import com.shyiko.coursework.model.RecipeAllergy;
import com.shyiko.coursework.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    @Autowired
    private UserService userService;

    @Autowired
    private RecipeDao recipeDao;

    @Autowired
    private RecipeAllergyDao recipeAllergyDao;

    @Autowired
    private UtilDao utilDao;

    public void saveRecipe(Recipe recipe) {
        recipeDao.save(recipe);
    }

    public Recipe createRecipe(String name, MultipartFile photo, String instruction, String ingredients, Time cookingTime, String complexity, Double price, String category, List<String> allergies) {
        byte[] photoBytes = null;
        try {
            photoBytes = photo.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Recipe recipe = new Recipe(name, photoBytes, instruction, ingredients, cookingTime, complexity, new BigDecimal(0), new BigDecimal(price), 0, category, userService.getCurrentUser());

        saveRecipe(recipe);
        if (allergies != null) for (String allergy : allergies) {
            recipeAllergyDao.save(new RecipeAllergy(recipe, (Allergy) utilDao.getByKey(Allergy.class, allergy)));
        }

        return recipe;
    }

    public List<Recipe> getAllRecipes() {
        return (List<Recipe>) recipeDao.getAll(Recipe.class);
    }

    public Recipe getRecipeById(Long id) {
        return (Recipe) recipeDao.getByKey(Recipe.class, id);
    }

    public void addVoted(Long recipeId) {
        Recipe recipe = getRecipeById(recipeId);
        recipe.setVoted(recipe.getVoted() + 1);
        recipeDao.save(recipe);
    }

    public void updateRating(Long recipeId) {
        recipeDao.updateRating(recipeId);
    }

    public List<Recipe> getRecipeByAuthor(String username) {
        return recipeDao.getRecipeByAuthor(username);
    }

    public Recipe updateRecipe(Long id, String name, MultipartFile photo, String instruction, String ingredients, Time cookingTime, String complexity, Double price, String category, List<String> allergies) {
        byte[] photoBytes = null;
        try {
            photoBytes = photo.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Recipe recipe = (Recipe) recipeDao.getByKey(Recipe.class, id);
        recipe.setName(name);
        recipe.setPhoto(photoBytes);
        recipe.setInstruction(instruction);
        recipe.setIngredients(ingredients);
        recipe.setCookingTime(cookingTime);
        recipe.setComplexity(complexity);
        recipe.setPrice(new BigDecimal(price));
        recipe.setCategory(category);
        recipeDao.update(recipe);
        recipeAllergyDao.removeExisting(recipe);
        if (allergies == null) return recipe;
        for (String allergy : allergies) {
            recipeAllergyDao.save(new RecipeAllergy(recipe, (Allergy) utilDao.getByKey(Allergy.class, allergy)));
        }

        return recipe;
    }

    public void deleteRecipe(Long id) {
        recipeDao.deleteRecipe(id);
    }

    public List<Recipe> getAllRecipes(Double minPrice, Double maxPrice, List<String> categories, LocalTime minTime, LocalTime maxTime, List<String> complexities, List<String> allergies, String searchPrompt) {
        List<Recipe> recipes = getAllRecipes();
        List<Recipe> excludedRecipes = new ArrayList<>();

        BigDecimal bigDecimalMinPrice = minPrice == null ? BigDecimal.ZERO : BigDecimal.valueOf(minPrice);
        BigDecimal bigDecimalMaxPrice = maxPrice == null ? BigDecimal.valueOf(Integer.MAX_VALUE) : BigDecimal.valueOf(maxPrice);
        recipes = recipes.stream().filter(x -> x.getPrice().compareTo(bigDecimalMinPrice) >= 0 && x.getPrice().compareTo(bigDecimalMaxPrice) <= 0).collect(Collectors.toList());

        Time validatedMinTime = minTime == null ? Time.valueOf(LocalTime.MIN) : Time.valueOf(minTime);
        Time validatedMaxTime = minTime == null ? Time.valueOf(LocalTime.MAX) : Time.valueOf(maxTime);
        recipes = recipes.stream().filter(x -> x.getCookingTime().compareTo(validatedMinTime) >= 0 && x.getCookingTime().compareTo(validatedMaxTime) <= 0).collect(Collectors.toList());

        if (categories != null && categories.size() > 0) {
            recipes = recipes.stream().filter(x -> categories.contains(x.getCategory())).collect(Collectors.toList());
        }

        if (complexities != null && complexities.size() > 0) {
            recipes = recipes.stream().filter(x -> complexities.contains(x.getComplexity())).collect(Collectors.toList());
        }
        if (allergies != null && !allergies.isEmpty()) {
            for (Recipe recipe : recipes) {
                List<String> recipeAllergies = recipeAllergyDao.getRecipeAllergies(recipe.getId()).stream().map(x -> x.getAllergy().getName()).toList();

                if (!Collections.disjoint(recipeAllergies, allergies)) {
                    excludedRecipes.add(recipe);
                }
            }

            recipes.removeAll(excludedRecipes);
        }

        if (searchPrompt != null && searchPrompt.length() > 0) {
            recipes = recipes.stream().filter(x -> x.getName().contains(searchPrompt)).collect(Collectors.toList());
        }

        return recipes;
    }

    public List<Recipe> getFilteredRecipes(String username, String sort, String search) {
        List<Recipe> result = recipeDao.getRecipeByAuthor(username);
        if (search != null && search.length() > 0)
            result = result.stream().filter(x -> x.getName().contains(search)).collect(Collectors.toList());
        if (sort != null) switch (sort) {
            case "Name" -> result.sort(Comparator.comparing(Recipe::getName));
            case "Rating" -> result.sort((o1, o2) -> o2.getRating().compareTo(o1.getRating()));
        }
        return result;
    }

    public List<Recipe> getTopRecipes(int topRecipes) {
        List<Recipe> recipes = (List<Recipe>) recipeDao.getAll(Recipe.class);
        recipes.sort((o1, o2) -> o2.getRating().compareTo(o1.getRating()));
        int lastElem = Math.min(topRecipes, recipes.size());
        return recipes.subList(0, lastElem);
    }

    public int getPagesAmount(List<Recipe> recipes) {
        return (int) Math.ceil((double) recipes.size() / (double) Constants.PAGE_SIZE);
    }

    public List<Recipe> getRecipePage(List<Recipe> recipes, Integer page) {
        if (page == null) page = 0;
        int startIndex = page * Constants.PAGE_SIZE;
        int endIndex = Math.min(startIndex + Constants.PAGE_SIZE, recipes.size());
        if (startIndex > endIndex) return new ArrayList<>();
        return recipes.subList(startIndex, endIndex);
    }

    public List<Recipe> sortRecipes(List<Recipe> filteredRecipes, String sort) {
        if (sort != null) switch (sort) {
            case "Name" -> filteredRecipes.sort(Comparator.comparing(Recipe::getName));
            case "Rating" -> filteredRecipes.sort((o1, o2) -> o2.getRating().compareTo(o1.getRating()));
            case "Price" -> filteredRecipes.sort((o1, o2) -> o2.getPrice().compareTo(o1.getPrice()));
        }
        return filteredRecipes;
    }
}
