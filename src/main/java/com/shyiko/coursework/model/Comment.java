package com.shyiko.coursework.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.engine.internal.Cascade;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserProfile user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @Column(name = "comment_text", nullable = false)
    private String commentText;

    @Column(name = "post_time", columnDefinition = "timestamp default now()")
    private Timestamp postTime;

    public Comment(UserProfile user, Recipe recipe, String commentText, Timestamp postTime) {
        this.user = user;
        this.recipe = recipe;
        this.commentText = commentText;
        this.postTime = postTime;
    }
}
