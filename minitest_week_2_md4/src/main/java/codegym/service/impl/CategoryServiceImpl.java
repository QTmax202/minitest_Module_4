package codegym.service.impl;

import codegym.model.Category;
import codegym.repository.ICategoryRepository;
import codegym.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public Iterable<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Optional<Category> findById(long id) {
        if (categoryRepository.findById(id).isPresent()) {
            return Optional.of(categoryRepository.findById(id).get());
        }
        return Optional.empty();
    }


}
