package com.example.minitest_week_3_md4.service.product;

import com.example.minitest_week_3_md4.model.Product;
import com.example.minitest_week_3_md4.repository.ICartegoryRepository;
import com.example.minitest_week_3_md4.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;

@Service
public class ProductService implements IProductService{
    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private ICartegoryRepository cartegoryRepository;

    @Override
    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<Product> findPage(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Iterable<Product> findAllByName(String name) {
        return productRepository.findAllByNameContaining(name);
    }

    @Override
    public Iterator<Product> findAllByCategory(Long id) {
        return productRepository.findProductByCategory(cartegoryRepository.findById(id));
    }
}
