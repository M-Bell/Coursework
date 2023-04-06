package com.shyiko.coursework.controller;

import com.shyiko.coursework.model.Allergy;
import com.shyiko.coursework.service.RecipeService;
import com.shyiko.coursework.service.UserService;
import com.shyiko.coursework.service.UtilService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RecipeController {
    @Autowired
    private UtilService utilService;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/create_recipe")
    String createRecipeForm(Model model) {
        if (!userService.isLoggedIn()) return "login";
        model.addAttribute("current_user", userService.getCurrentUser());
        model.addAttribute("imageData", userService.getCurrentUserPhoto());
        model.addAttribute("categories", utilService.getAllCategories());
        model.addAttribute("complexities", Arrays.asList("Easy", "Moderate", "Advanced"));
        model.addAttribute("allergies", utilService.getAllAllergies());
        return "create_recipe";
    }

    @PostMapping("/create_recipe")
    String submitRecipe(@RequestParam("name") String name,
                        @RequestParam("photo") MultipartFile photo,
                        @RequestParam("instruction") String instruction,
                        @RequestParam("ingredients") String ingredients,
                        @RequestParam("cooking_time") LocalTime cookingTime,
                        @RequestParam("complexity") String complexity,
                        @RequestParam("price") Double price,
                        @RequestParam("category") String category,
                        @RequestParam(value = "allergies", required = false) List<String> allergies) {
        if (!userService.isLoggedIn()) return "login";
        if (null != recipeService.createRecipe(name, photo, instruction, ingredients, new Time(cookingTime.getHour(), cookingTime.getMinute(), cookingTime.getSecond()), complexity, price, category, allergies)) {
            return "redirect:/account/" + userService.getCurrentUser().getUsername();
        }
        return "create_recipe";
    }

    @PostMapping("/edit_recipe/{id}")
    String editRecipe(@PathVariable("id") Long id,
                      @RequestParam("name") String name,
                      @RequestParam("photo") MultipartFile photo,
                      @RequestParam("instruction") String instruction,
                      @RequestParam("ingredients") String ingredients,
                      @RequestParam("cooking_time") LocalTime cookingTime,
                      @RequestParam("complexity") String complexity,
                      @RequestParam("price") Double price,
                      @RequestParam("category") String category,
                      @RequestParam(value = "allergies", required = false) List<String> allergies) {
        if (!userService.isLoggedIn()) return "login";
        if (null != recipeService.updateRecipe(id, name, photo, instruction, ingredients, new Time(cookingTime.getHour(), cookingTime.getMinute(), cookingTime.getSecond()), complexity, price, category, allergies)) {
            return "redirect:/account/" + userService.getCurrentUser().getUsername();
        }
        return "edit_recipe";
    }

    @GetMapping("/edit_recipe/{id}")
    String editRecipeForm(@PathVariable Long id, Model model) {
        if (!userService.isLoggedIn()) return "login";
        model.addAttribute("recipe", recipeService.getRecipeById(id));
        model.addAttribute("current_user", userService.getCurrentUser());
        model.addAttribute("imageData", userService.getCurrentUserPhoto());
        model.addAttribute("allergies", utilService.getAllAllergies());
        model.addAttribute("categories", utilService.getAllCategories());
        model.addAttribute("complexities", Arrays.asList("Easy", "Moderate", "Advanced"));
        model.addAttribute("chosen_allergies", utilService.getAllergiesById(id).stream().map(Allergy::getName).collect(Collectors.toList()));
        return "edit_recipe";
    }

    @PostMapping("/rate-recipe/{recipeId}/{rating}")
    public String rateRecipe(@PathVariable("recipeId") Long id, @PathVariable("rating") Integer rating) {
        utilService.rateRecipe(id, rating);
        return "redirect:/recipe/" + id; // redirect to the recipe page
    }

    @PostMapping("/delete_recipe/{recipeId}")
    public String deleteRecipe(@PathVariable("recipeId") Long id) {
        recipeService.deleteRecipe(id);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}
