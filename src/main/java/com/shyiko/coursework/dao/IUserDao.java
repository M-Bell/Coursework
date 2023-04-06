package com.shyiko.coursework.dao;

import com.shyiko.coursework.model.UserProfile;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserDao {
    void saveUser(UserProfile user);

    UserProfile getUserByUsername(String username);

    UserProfile getUserById(Long id);

}
