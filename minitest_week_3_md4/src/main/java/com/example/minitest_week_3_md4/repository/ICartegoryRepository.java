package com.example.minitest_week_3_md4.repository;

import com.example.minitest_week_3_md4.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICartegoryRepository extends CrudRepository<Category, Long> {
}
