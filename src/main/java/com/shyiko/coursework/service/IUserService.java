package com.shyiko.coursework.service;

import com.shyiko.coursework.model.Gender;
import com.shyiko.coursework.model.UserProfile;

import java.time.LocalDate;

public interface IUserService {
    void saveUser(UserProfile user);

    boolean isUsernameAvailable(String username);

    UserProfile registerUser(String username,
                             String password,
                             String gender,
                             LocalDate date, byte[] photo,
                             String fullName, String bio);

    UserProfile authenticateUser(String username, String password);

    UserProfile getUserById(Long id);

    String getCurrentUserPhoto();

    UserProfile getCurrentUser();

    boolean isLoggedIn();
}
