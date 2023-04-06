package com.shyiko.coursework.controller;


import com.shyiko.coursework.service.RecipeService;
import com.shyiko.coursework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Controller
public class UserAccountController {
    @Autowired
    private UserService userService;

    @Autowired
    private RecipeService recipeService;

    @GetMapping("account/{username}")
    String showUserPage(@PathVariable String username,
                        @RequestParam(required = false, value = "sort") String sort,
                        @RequestParam(required = false, value = "search") String search,
                        Model model) {
        if (!userService.isLoggedIn()) return "login";
        model.addAttribute("current_user", userService.getCurrentUser());
        model.addAttribute("account_user", userService.getUserByUsername(username));
        model.addAttribute("recipes", recipeService.getFilteredRecipes(username, sort, search));
        return "account";
    }

    @GetMapping("edit_user/{username}")
    String editUserPage(@PathVariable String username, Model model) {
        if (!userService.isLoggedIn()) return "login";
        model.addAttribute("edit_user", userService.getUserByUsername(username));
        model.addAttribute("username", username);
        return "edit_user";
    }

    @PostMapping("edit_user/{username}")
    String editUser(@PathVariable("username") String username,
                    @RequestParam("full-name") String fullName,
                    @RequestParam("birthday") LocalDate date,
                    @RequestParam("photo") MultipartFile photo,
                    @RequestParam("bio") String bio,
                    @RequestParam("gender") String gender,
                    Model model) {
        if (!userService.isLoggedIn()) return "login";
        if (null != userService.updateUser(username, fullName, date, photo, bio, gender)) {
            return "redirect:/account/" + username;
        }

        return "edit_user";
    }

    @PostMapping("logout")
    String logout() {
        if (!userService.isLoggedIn()) return "login";
userService.logout();
        return "login";
    }
}
