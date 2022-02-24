package cg.service;

import cg.model.Product;

import java.util.ArrayList;

public interface IProductService {
    ArrayList<Product> getAllProduct();

    Product saveProduct(Product product);

    Product deleteProduct(int id);

    Product getProduct(int id);

    ArrayList<Product> getAllProductByName(String name);
}
