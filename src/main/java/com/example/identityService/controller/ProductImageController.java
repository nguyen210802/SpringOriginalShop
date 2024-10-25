package com.example.identityService.controller;

import com.example.identityService.dto.ApiResponse;
import com.example.identityService.entity.ProductImage;
import com.example.identityService.service.ProductImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/product/image")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductImageController {
    ProductImageService productImageServiceImpl;

    @GetMapping("/getByProduct/{productId}")
    public ApiResponse<List<ProductImage>> getByProduct(@PathVariable("productId") String productId){
        return ApiResponse.<List<ProductImage>>builder()
                .result(productImageServiceImpl.getAllByproductId(productId))
                .build();
    }

    @GetMapping("/{productImageId}")
    public ApiResponse<ProductImage> getById(@PathVariable("productImageId") String id){
        return ApiResponse.<ProductImage>builder()
                .result(productImageServiceImpl.getById(id))
                .build();
    }
}
