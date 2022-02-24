package cg.repository;

import cg.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface IProductRepository extends CrudRepository<Product, Integer> {
    Iterable<Product> findAllByNameContaining(String name);
}
