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
public class CommentDao extends BaseDao{

    public List<Comment> getAllCommentsByRecipeId(Long id) {
        TypedQuery<Comment> query = entityManager.createQuery(
                "SELECT u from Comment u WHERE u.recipe.id = :id", Comment.class);
        query.setParameter("id", id);
        return query.getResultList();
    }
}
