package com.shyiko.coursework.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "recipe_allergy")
@Getter
@Setter
@NoArgsConstructor
public class RecipeAllergy implements Serializable {

    @EmbeddedId
    private RecipeAllergyId id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("recipe_id")
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @MapsId("allergy")
    @JoinColumn(name = "allergy")
    private Allergy allergy;

    public RecipeAllergy(Recipe recipe, Allergy allergy) {
        this.id = new RecipeAllergyId(recipe.getId(), allergy.getName());
        this.recipe = recipe;
        this.allergy = allergy;
    }

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipeAllergyId implements Serializable {

        @Column(name = "recipe_id")
        private Long recipeId;

        @Column(name = "allergy")
        private String allergy;

        // constructors, equals and hashCode methods
    }
}