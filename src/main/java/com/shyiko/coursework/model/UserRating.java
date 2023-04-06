package com.shyiko.coursework.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "user_ratings")
@Getter
@Setter
@NoArgsConstructor
public class UserRating {

    @EmbeddedId
    private UserRatingId id;

    @ManyToOne
    @MapsId("userId")
    private UserProfile user;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("recipeId")
    private Recipe recipe;

    @Column(name = "rating", precision = 3, scale = 2)
    private BigDecimal rating;

    public UserRating(UserProfile user, Recipe recipe, BigDecimal rating) {
        this.id = new UserRatingId(user.getId(), recipe.getId());
        this.user = user;
        this.recipe = recipe;
        this.rating = rating;
    }
// constructors, getters and setters

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRatingId implements Serializable {

        @Column(name = "user_id")
        private Long userId;

        @Column(name = "recipe_id")
        private Long recipeId;
        // constructors, equals and hashCode methods
    }
}

