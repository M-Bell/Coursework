package com.shyiko.coursework.service;

import com.shyiko.coursework.model.Allergy;
import com.shyiko.coursework.model.Category;

import java.util.List;

public interface IUtilService {
    List<String> getAllCategories();

    List<String> getAllAllergies();

    Category getCategoryByName(String name);

    Allergy getAllergyByName(String name);
}
