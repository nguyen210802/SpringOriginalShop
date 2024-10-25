package com.example.identityService.service;

import com.example.identityService.dto.PageResponse;
import com.example.identityService.dto.request.ProductRequest;
import com.example.identityService.entity.Product;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    PageResponse<Product> getAll(int page, int size);
    Product getProductById(String id);
    PageResponse<Product> getAllMyProduct(int page, int size);
    PageResponse<Product> getAllByName(int page, int size, String name);
    Product create(ProductRequest request) throws IOException;
    Product update(String id, Product product);
    String delete(String id);
}
