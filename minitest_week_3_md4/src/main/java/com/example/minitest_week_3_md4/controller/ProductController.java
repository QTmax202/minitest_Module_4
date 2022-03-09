package com.example.minitest_week_3_md4.controller;

import com.example.minitest_week_3_md4.model.Category;
import com.example.minitest_week_3_md4.model.Product;
import com.example.minitest_week_3_md4.service.category.ICategoryService;
import com.example.minitest_week_3_md4.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
@CrossOrigin("*")
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @Value("${upload_file}")
    private String upload_file;

    @GetMapping("/product/{page}")
    public ResponseEntity<Page<Product>> showProducts(@PageableDefault(value = 5) Pageable pageable){
        Page<Product> products = productService.findPage(pageable);
        if (!products.iterator().hasNext()){
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/cate")
    public ResponseEntity<Iterable<Category>> showAllCate() {
        Iterable<Category> categories = categoryService.findAll();
        if (!categories.iterator().hasNext()) {
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> showOne(@PathVariable("id") Long id) {
        Optional<Product> product = productService.findById(id);
        if (!product.isPresent()) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product.get(), HttpStatus.OK);
    }

    @PostMapping("/product/save")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        String fileName = product.getImageFile().getOriginalFilename();
        try {
            FileCopyUtils.copy(product.getImageFile().getBytes(), new File(upload_file + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        product.setImageUrl("images/"+ fileName);
        Product productCreate = productService.save(product);
        return new ResponseEntity<>(productCreate, HttpStatus.CREATED);
    }

    @PutMapping("/product/edit/{id}")
    public ResponseEntity<Product> editProduct(@RequestBody Product productEdit, @PathVariable("id") Long id) {
        Optional<Product> product = productService.findById(id);
        String fileName = productEdit.getImageFile().getOriginalFilename();
        try {
            FileCopyUtils.copy(productEdit.getImageFile().getBytes(), new File(upload_file + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        productEdit.setImageUrl("images/"+ fileName);
        if (!product.isPresent()) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (productEdit.getImageFile() != null){
            productEdit.setImageFile(product.get().getImageFile());
        }
        productEdit.setId(product.get().getId());
        productEdit = productService.save(productEdit);
        return new ResponseEntity<>(productEdit, HttpStatus.OK);
    }

    @DeleteMapping("/product/delete/{id}")
    public ResponseEntity<Product> delete(@PathVariable("id") Long id) {
        Optional<Product> product = productService.findById(id);
        if (!product.isPresent()) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.delete(id);
        return new ResponseEntity<>(product.get(), HttpStatus.OK);
    }

    @GetMapping("/product/search")
    public ResponseEntity<Iterable<Product>> showAllByName(@RequestParam("search") String search) {
        Iterable<Product> products = productService.findAllByName(search);
        if (!products.iterator().hasNext()) {
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
