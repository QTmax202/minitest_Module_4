package codegym.service;

import codegym.model.Category;

import java.util.Optional;

public interface ICategoryService {
    Iterable<Category> findAllCategories();

    Category saveCategory(Category category);

    void deleteCategory(long id);

    Optional<Category> findById(long id);
}
