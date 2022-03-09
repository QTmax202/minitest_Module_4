package com.example.minitest_week_3_md4.repository;

import com.example.minitest_week_3_md4.model.Category;
import com.example.minitest_week_3_md4.model.Product;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.Optional;

@Repository
public interface IProductRepository extends PagingAndSortingRepository<Product, Long> {
    Iterable<Product> findAllByNameContaining(String name);

    Iterator<Product> findProductByCategory(Optional<Category> category);
}
