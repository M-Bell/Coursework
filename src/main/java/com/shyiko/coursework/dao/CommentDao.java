package com.shyiko.coursework.dao;

import com.shyiko.coursework.model.Comment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
@Component
public class CommentDao {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private EntityTransaction transaction;

    public void saveComment(Comment comment) {
        try {
            // Begin transaction
            transaction.begin();

            // Save user to the database
            entityManager.persist(comment);

            // Commit transaction
            transaction.commit();

            System.out.println("User saved successfully");
        } catch (Exception e) {
            // Rollback transaction if there is an error
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

    }

    public List<Comment> getAllComments() {
        TypedQuery<Comment> query = entityManager.createQuery(
                "SELECT u from Comment u", Comment.class);
        return query.getResultList();
    }

    public List<Comment> getAllCommentsByRecipeId(Long id) {
        TypedQuery<Comment> query = entityManager.createQuery(
                "SELECT u from Comment u WHERE u.recipe.id = :id", Comment.class);
        query.setParameter("id", id);
        return query.getResultList();
    }
}
