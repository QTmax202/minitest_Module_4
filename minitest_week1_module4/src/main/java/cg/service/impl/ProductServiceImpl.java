package cg.service.impl;

import cg.model.Product;
import cg.repository.IProductRepository;
import cg.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductRepository repository;

    @Override
    public ArrayList<Product> getAllProduct() {
        return (ArrayList<Product>) repository.findAll();
    }

    @Override
    public Product saveProduct(Product product) {
        return repository.save(product);
    }

    @Override
    public Product deleteProduct(int id) {
        Product product = getProduct(id);
        if (product != null) {
            repository.delete(product);
            return product;
        }
        return null;
    }

    @Override
    public Product getProduct(int id) {
        if (repository.findById(id).isPresent()) {
            return repository.findById(id).get();
        }
        return null;
    }

    @Override
    public ArrayList<Product> getAllProductByName(String name) {
        return (ArrayList<Product>) repository.findAllByNameContaining(name);
    }
}