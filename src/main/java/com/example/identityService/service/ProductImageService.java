package com.example.identityService.service;

import com.example.identityService.entity.Product;
import com.example.identityService.entity.ProductImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductImageService {
    List<ProductImage> getAllByproductId(String productId);
    ProductImage create(MultipartFile image, Product product) throws IOException;
    ProductImage getById(String productImageId);
    ProductImage update(ProductImage productImage);
    String delete(String productImageId);
}
