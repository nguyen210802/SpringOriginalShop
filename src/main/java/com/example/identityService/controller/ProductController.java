package com.example.identityService.controller;

import com.example.identityService.dto.ApiResponse;
import com.example.identityService.dto.PageResponse;
import com.example.identityService.dto.request.ProductRequest;
import com.example.identityService.entity.Cart;
import com.example.identityService.entity.Product;
import com.example.identityService.service.CartService;
import com.example.identityService.service.ProductService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users/product")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductController {
    Map<String, Object> map;

    public ProductController(ProductService productServiceImpl, CartService cartServiceImpl) {
        this.map = Map.of("product", productServiceImpl, "cart", cartServiceImpl);
    }

    @GetMapping("/getAll")
    public ApiResponse<PageResponse<Product>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        ProductService productService = (ProductService) map.get("product");

        return ApiResponse.<PageResponse<Product>>builder()
                .result(productService.getAll(page, size))
                .build();
    }

    @GetMapping("/getAllByName")
    public ApiResponse<PageResponse<Product>> getAllByName(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam String name) {

        ProductService productService = (ProductService) map.get("product");

        return ApiResponse.<PageResponse<Product>>builder()
                .result(productService.getAllByName(page, size, name))
                .build();
    }

    @GetMapping("/{productId}")
    public ApiResponse<Product> getProductById(@PathVariable("productId") String productId){
        ProductService productService = (ProductService) map.get("product");
        return ApiResponse.<Product>builder()
                .result(productService.getProductById(productId))
                .build();
    }

    @GetMapping("/myProduct")
    public ApiResponse<PageResponse<Product>> getMyProduct(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "10") int size){
        ProductService productService = (ProductService) map.get("product");
        return ApiResponse.<PageResponse<Product>>builder()
                .result(productService.getAllMyProduct(page, size))
                .build();
    }


    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Product> createProduct(@ModelAttribute ProductRequest request) throws IOException {
        ProductService productService = (ProductService) map.get("product");
        return ApiResponse.<Product>builder()
                .result(productService.create(request))
                .build();
    }

    @PutMapping("/update/{productId}")
    public ApiResponse<Product> updateProduct(@PathVariable("productId") String productId, @RequestBody Product request){
        log.info("Product update Controller");
        ProductService productService = (ProductService) map.get("product");
        return ApiResponse.<Product>builder()
                .result(productService.update(productId, request))
                .build();
    }

    @DeleteMapping("/delete/{productId}")
    public ApiResponse<String> deleteProduct(@PathVariable("productId") String id){
        ProductService productService = (ProductService) map.get("product");
        return ApiResponse.<String>builder()
                .result(productService.delete(id))
                .build();
    }
}
