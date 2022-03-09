package codegym.service;

import codegym.model.Category;
import codegym.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService {
    Page<Product> findAllProducts(Pageable pageable);

    Product saveProduct(Product product);

    void deleteProduct(long id);

    Product findById(long id);

    Page<Product> getAllProductsByName(String name, Pageable pageable);

    Page<Product> getAllProductsByCategory(Category category, Pageable pageable);
}
