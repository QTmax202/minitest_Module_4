package com.example.minitest_week_3_md4.service.product;

import com.example.minitest_week_3_md4.model.Product;
import com.example.minitest_week_3_md4.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Iterator;

public interface IProductService extends IGeneralService<Product> {
    Page<Product> findPage(Pageable pageable);

    Iterable<Product> findAllByName(String name);

    Iterator<Product> findAllByCategory(Long id);
}
