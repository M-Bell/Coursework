package com.shyiko.coursework.controller;

import com.shyiko.coursework.model.Recipe;
import com.shyiko.coursework.service.CommentService;
import com.shyiko.coursework.service.RecipeService;
import com.shyiko.coursework.service.UserService;
import com.shyiko.coursework.service.UtilService;
import com.shyiko.coursework.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private UserService userService;

    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UtilService utilService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/")
    String main(@RequestParam(required = false, value = "page") Integer page,
                @RequestParam(required = false, value = "minPrice") Double minPrice,
                @RequestParam(required = false, value = "maxPrice") Double maxPrice,
                @RequestParam(required = false, value = "categories") List<String> categories,
                @RequestParam(required = false, value = "minTime") LocalTime minTime,
                @RequestParam(required = false, value = "maxTime") LocalTime maxTime,
                @RequestParam(required = false, value = "complexities") List<String> complexities,
                @RequestParam(required = false, value = "allergies") List<String> allergies,
                @RequestParam(required = false, value = "search") String searchPrompt,
                @RequestParam(required = false, value = "form-sort") String sort,
                Model model) {
        if (!userService.isLoggedIn()) return "redirect:/login";
        if(page==null) page = 0;
        List<Recipe> filteredRecipes =recipeService.getAllRecipes(minPrice, maxPrice, categories, minTime, maxTime, complexities, allergies, searchPrompt);
        filteredRecipes = recipeService.sortRecipes(filteredRecipes, sort);
        model.addAttribute("current_user", userService.getCurrentUser());
        model.addAttribute("imageData", userService.getCurrentUserPhoto());
        model.addAttribute("chosenComplexities", complexities);
        model.addAttribute("chosen_categories", categories);
        model.addAttribute("categories", utilService.getAllCategories());
        model.addAttribute("complexities", Arrays.asList("Easy", "Moderate", "Advanced"));
        model.addAttribute("allergies", utilService.getAllAllergies());
        model.addAttribute("chosenAllergies", allergies);
        model.addAttribute("recipes", recipeService.getRecipePage(filteredRecipes, page));
        model.addAttribute("links", recipeService.getPagesAmount(filteredRecipes));
        model.addAttribute("current_page", page);
        model.addAttribute("current_sort", sort);
        return "recipes";
    }


    @GetMapping("/recipe/{id}")
    String showRecipe(@PathVariable Long id, Model model) {
        if (!userService.isLoggedIn()) return "redirect:/login";
        model.addAttribute("current_user", userService.getCurrentUser());
        model.addAttribute("recipe", recipeService.getRecipeById(id));
        model.addAttribute("comments", commentService.getAllRelevantCommentsByRecipeId(id));
        model.addAttribute("chosen_rating", utilService.getRating(id, userService.getCurrentUser()));
        return "current_recipe";
    }

    @PostMapping("/post-comment/{id}")
    String addComment(@RequestParam("comment") String comment, @PathVariable("id") Long id) {
        if (comment != null) commentService.saveComment(id, comment);
        return "redirect:/recipe/" + id; // redirect to the recipe page
    }

    @GetMapping("/top-chefs")
    String showTopChefs(Model model) {
        model.addAttribute("current_user", userService.getCurrentUser());
        model.addAttribute("users", userService.getTopUsers(Constants.TOP_USERS));
        return "top-chefs";
    }

    @GetMapping("/top-recipes")
    String showTopRecipes(Model model) {
        model.addAttribute("current_user", userService.getCurrentUser());
        model.addAttribute("recipes", recipeService.getTopRecipes(Constants.TOP_RECIPES));
        return "top-recipes";
    }
}
