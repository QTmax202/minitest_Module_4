package codegym.service.impl;

import codegym.model.Category;
import codegym.model.Product;
import codegym.repository.IProductRepository;
import codegym.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private IProductRepository productRepository;

    @Override
    public Page<Product> findAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product findById(long id) {
        if (productRepository.findById(id).isPresent()) {
            return productRepository.findById(id).get();
        }
        return null;
    }

    @Override
    public Page<Product> getAllProductsByName(String name, Pageable pageable) {
        return  productRepository.findAllByNameContaining(name, pageable);
    }

    @Override
    public Page<Product> getAllProductsByCategory(Category category, Pageable pageable) {
        return productRepository.findAllByCategory(category, pageable);
    }
}
