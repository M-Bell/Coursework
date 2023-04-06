package com.shyiko.coursework.service;

import com.shyiko.coursework.dao.RecipeDao;
import com.shyiko.coursework.dao.UserDao;
import com.shyiko.coursework.model.Role;
import com.shyiko.coursework.model.UserProfile;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RecipeDao recipeDao;

    @Autowired
    private HttpServletRequest request;

    @Override
    @Transactional
    public void saveUser(UserProfile user) {
        userDao.saveUser(user);
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        UserProfile user = userDao.getUserByUsername(username);
        return user == null;
    }

    @Override
    public UserProfile registerUser(String username, String password, String gender, LocalDate date, byte[] photo, String fullName, String bio) {

        UserProfile userProfile = new UserProfile(username, fullName, password, photo, date, bio, gender, Role.USER);
        saveUser(userProfile);
        return userProfile;
    }

    @Override
    public UserProfile authenticateUser(String username, String password) {
        UserProfile user = userDao.getUserByUsername(username);
        if (user != null && password.equals(user.getPassword())) {
            request.getSession().setAttribute("currentUser", user);
            return user;
        }
        return null;
    }

    @Override
    public UserProfile getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Override
    public String getCurrentUserPhoto() {
        UserProfile userProfile = (UserProfile) request.getSession().getAttribute("currentUser");
        byte[] data = null;
        if (userProfile != null && userProfile.getProfilePhoto() != null && userProfile.getProfilePhoto().length > 0) {
            data = userProfile.getProfilePhoto();
        } else {
            try {
                BufferedImage bImage = ImageIO.read(new File("src/main/resources/static/images/no-avatar-300x300.png"));
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(bImage, "png", bos);
                data = bos.toByteArray();
            } catch (IOException ignored) {
            }
        }
        return data == null ? null : Base64.getEncoder().encodeToString(data);

    }

    @Override
    public UserProfile getCurrentUser() {
        return (UserProfile) request.getSession().getAttribute("currentUser");
    }

    @Override
    public boolean isLoggedIn() {
        return request.getSession().getAttribute("currentUser") != null;
    }

    public Object updateUser(String username, String fullName, LocalDate date, MultipartFile photo, String bio, String gender) {
        byte[] photoBytes = null;
        try {
            photoBytes = photo.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UserProfile userProfile = userDao.getUserByUsername(username);
        userProfile.setFname(fullName);
        userProfile.setGender(gender);
        userProfile.setBirthday(date);
        userProfile.setBio(bio);
        if (photoBytes != null) userProfile.setProfilePhoto(photoBytes);
        userDao.updateUser(userProfile);
        return userProfile;
    }

    public UserProfile getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    public List<UserProfile> getTopUsers(int topUsers) {
        List<UserProfile> recipes = userDao.getAllUsers();

        recipes.sort((o1, o2) -> Double.compare(recipeDao.getRecipeByAuthor(
                o1.getUsername()).stream().mapToDouble(
                x -> x.getRating().doubleValue()).average().orElse(0), recipeDao.getRecipeByAuthor(o2.getUsername()).stream().mapToDouble(y -> y.getRating().doubleValue()).average().orElse(0)));
        int lastElem = Math.min(topUsers, recipes.size());
        return recipes.subList(0, lastElem);
    }

    public void logout() {
        request.getSession().removeAttribute("currentUser");
    }
}