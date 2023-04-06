package com.shyiko.coursework.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Base64;


@Entity
@Table(name = "recipes")
@NoArgsConstructor
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "photo", columnDefinition = "bytea")
    private byte[] photo;

    @Column(name = "instruction", nullable = false)
    private String instruction;

    @Column(name = "ingredients", nullable = false)
    private String ingredients;

    @Column(name = "cooking_time")
    private Time cookingTime;


    @Column(name = "complexity")
    private String complexity;

    @Column(name = "rating", precision = 3, scale = 2, columnDefinition = "numeric(3,2) default 0.00")
    private BigDecimal rating = BigDecimal.ZERO;

    @Column(name = "price", precision = 5, scale = 2)
    private BigDecimal price;

    @Column(name = "voted", columnDefinition = "int default 0")
    private Integer voted = 0;

    @Column(name = "category", nullable = false, length = 15)
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserProfile author;

    public Recipe(String name, byte[] photo, String instruction, String ingredients, Time cookingTime, String complexity, BigDecimal rating, BigDecimal price, Integer voted, String category, UserProfile author) {
        this.name = name;
        this.photo = photo;
        this.instruction = instruction;
        this.ingredients = ingredients;
        this.cookingTime = cookingTime;
        this.complexity = complexity;
        this.rating = rating;
        this.price = price;
        this.voted = voted;
        this.category = category;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public String getEncoded(){
        if(photo!=null) return Base64.getEncoder().encodeToString(photo);
        return "";
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public Time getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(Time cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getComplexity() {
        return complexity;
    }

    public void setComplexity(String complexity) {
        this.complexity = complexity;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getVoted() {
        return voted;
    }

    public void setVoted(Integer voted) {
        this.voted = voted;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public UserProfile getAuthor() {
        return author;
    }

    public void setAuthor(UserProfile author) {
        this.author = author;
    }
}