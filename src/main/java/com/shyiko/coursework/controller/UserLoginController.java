package com.shyiko.coursework.controller;

import com.shyiko.coursework.model.Gender;
import com.shyiko.coursework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@Controller
public class UserLoginController {
    @Autowired
    UserService userService;

    @GetMapping("/login")
    String login() {
        return "login";
    }

    // POST method for processing the login form
    @PostMapping("/login")
    public String loginUser(@RequestParam("name") String username, @RequestParam("password") String password, Model model) {
        if (userService.authenticateUser(username, password) != null) {
            return "redirect:/";
        } else {
            model.addAttribute("loginError", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/register")
    String register() {
        return "register";
    }

    // POST method for processing the login form
    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("full-name") String fullName,
                               @RequestParam("birthday") LocalDate date,
                               @RequestParam("photo") MultipartFile photo,
                               @RequestParam("bio") String bio,
                               @RequestParam("gender") String gender,
                               Model model) throws IOException {

        if (!userService.isUsernameAvailable(username)) {
            model.addAttribute("loginError", "Username already exists");
            return "register";
        }
        if (userService.registerUser(username, password, gender, date, photo.getBytes(), fullName, bio) != null) {
            return "redirect:/login";
        }
        model.addAttribute("loginError", "Invalid username or password");
        return "register";

    }
}
