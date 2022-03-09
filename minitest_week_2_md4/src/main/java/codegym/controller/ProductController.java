package codegym.controller;

import codegym.model.Category;
import codegym.model.Product;
import codegym.service.ICategoryService;
import codegym.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Value("${file-upload}")
    private String fileUpload;

    @Value("${view}")
    private String view;

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @GetMapping
    public ModelAndView showProducts(@PageableDefault(value = 1) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("product/list");
        Page<Product> products = productService.findAllProducts(pageable);
        if (products.isEmpty()) {
            modelAndView.addObject("message", "No products !!!");
        }
        modelAndView.addObject("file", view);
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/showCategories")
    public ModelAndView showCategories() {
        ModelAndView modelAndView = new ModelAndView("category/list");
        Iterable<Category> categories = categoryService.findAllCategories();
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }

    @GetMapping("create-product")
    public ModelAndView createProduct() {
        ModelAndView modelAndView = new ModelAndView("product/create");
        Iterable<Category> categories = categoryService.findAllCategories();
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("product", new Product());
        return modelAndView;
    }

    @GetMapping("/create-category")
    public ModelAndView createCategory() {
        ModelAndView modelAndView = new ModelAndView("category/create");
        modelAndView.addObject("category", new Category());
        return modelAndView;
    }

    @PostMapping("save-product")
    public ModelAndView saveProduct(@ModelAttribute Product product) {
        ModelAndView modelAndView = new ModelAndView("product/create");
        MultipartFile multipartFile = product.getImageFile();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(product.getImageFile().getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        product.setImageUrl(view + fileName);
        Product productCreate = productService.saveProduct(product);
        if (productCreate != null) {
            Iterable<Category> categories = categoryService.findAllCategories();
            modelAndView.addObject("categories", categories);
            modelAndView.addObject("message", "Create Product Successfully !!!");
        }
        return modelAndView;
    }

    @PostMapping("save-category")
    public ModelAndView saveCategory(@ModelAttribute Category category) {
        ModelAndView modelAndView = new ModelAndView("category/create");
        Category categoryCreate = categoryService.saveCategory(category);
        if (categoryCreate != null) {
            modelAndView.addObject("message", "Create Category Successfully !!!");
        }
        return modelAndView;
    }

    @GetMapping("/delete-product/{id}")
    public ModelAndView deleteProduct(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("redirect:/home");
        productService.deleteProduct(id);
        return modelAndView;
    }

    @GetMapping("/delete-category/{id}")
    public ModelAndView deleteCategory(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("category/list");
        categoryService.deleteCategory(id);
        Iterable<Category> categories = categoryService.findAllCategories();
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }

    @GetMapping("/view/{id}")
    public ModelAndView detailProduct(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("product/detail");
        Product product = productService.findById(id);
        modelAndView.addObject("product", product);
        modelAndView.addObject("file", view);
        return modelAndView;
    }

    @GetMapping("/edit-product/{id}")
    public ModelAndView editProduct(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("product/edit");
        Product product = productService.findById(id);
        Iterable<Category> categories = categoryService.findAllCategories();
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("product", product);
        modelAndView.addObject("file", view);
        return modelAndView;
    }

    @GetMapping("/edit-category/{id}")
    public ModelAndView editCategory(@PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("category/edit");
        Optional<Category> category = categoryService.findById(id);
        modelAndView.addObject("category", category);
        return modelAndView;
    }

    @PostMapping("/update-product/{id}")
    public ModelAndView updateProduct(@PathVariable long id, @ModelAttribute Product product) {
        ModelAndView modelAndView = new ModelAndView("/product/edit");
        product.setId(id);
        if (product.getImageFile().getSize() != 0) {
            MultipartFile multipartFile = product.getImageFile();
            String fileName = multipartFile.getOriginalFilename();
            try {
                FileCopyUtils.copy(product.getImageFile().getBytes(), new File(fileUpload + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            product.setImageUrl(view + fileName);
        } else {
            product.setImageUrl(productService.findById(id).getImageUrl());
        }
        Product productEdit = productService.saveProduct(product);
        if (productEdit != null) {
            Iterable<Category> categories = categoryService.findAllCategories();
            modelAndView.addObject("categories", categories);
            modelAndView.addObject("file", view);
            modelAndView.addObject("message", "Update Product Successfully !!!");
        }
        return modelAndView;
    }

    @PostMapping("/update-category")
    public ModelAndView updateCategory(@ModelAttribute Category category) {
        ModelAndView modelAndView = new ModelAndView("category/edit");
        Category categoryEdit = categoryService.saveCategory(category);
        if (categoryEdit != null) {
            modelAndView.addObject("message", "Update Category Successfully !!!");
        }
        return modelAndView;
    }

    @PostMapping("/searchByName")
    public ModelAndView searchProductByName(@RequestParam("searchByName") String name, @PageableDefault(value = 5) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("product/list");
        Page<Product> products;
        if (name != null) {
            products = productService.getAllProductsByName(name, pageable);
        } else {
            products = productService.findAllProducts(pageable);
        }

//        ArrayList<Product> products = productService.getAllProductsByName(name);
//        if (products.isEmpty()) {
//            modelAndView.addObject("message", "Don't search product have this name !!!");
//        }
        modelAndView.addObject("file", view);
        modelAndView.addObject("products", products);
        modelAndView.addObject("searchByName", name);
        return modelAndView;
    }

    @GetMapping("/view-category/{id}")
    public ModelAndView viewCategory(@PathVariable long id, @PageableDefault(value = 3) Pageable pageable) {
        ModelAndView modelAndView = new ModelAndView("category/view");
        Optional<Category> categoryOptional = categoryService.findById(id);
        if (!categoryOptional.isPresent()) {
            return new ModelAndView("error.404");
        }
        Page<Product> products = productService.getAllProductsByCategory(categoryOptional.get(), pageable);
        modelAndView.addObject("file", view);
        modelAndView.addObject("products", products);
//        modelAndView.addObject("category", categoryOptional.get());
        return modelAndView;
    }
}
