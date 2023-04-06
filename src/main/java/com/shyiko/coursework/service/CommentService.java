package com.shyiko.coursework.service;


import com.shyiko.coursework.dao.CommentDao;
import com.shyiko.coursework.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentDao commentDao;

    @Autowired
    private UserService userService;

    @Autowired
    private RecipeService recipeService;

    public void saveComment(Long id, String comment) {
        commentDao.saveComment(new Comment(userService.getCurrentUser(),
                recipeService.getRecipeById(id), comment,
                Timestamp.valueOf(LocalDateTime.now())));
    }

    public List<Comment> getAllComments() {
        return commentDao.getAllComments();
    }

    public List<Comment> getAllRelevantCommentsByRecipeId(Long id) {
        return commentDao.getAllCommentsByRecipeId(id);
    }
}
