package com.shyiko.coursework.dao;

import com.shyiko.coursework.model.Allergy;
import com.shyiko.coursework.model.Category;

import java.util.List;

public interface IUtilDao {
    List<Category> getAllCategories();

    List<Allergy> getAllAllergies();

    Category getCategoryByName(String name);

    Allergy getAllergyByName(String name);
}
