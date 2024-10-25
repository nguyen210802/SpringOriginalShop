package com.example.identityService.service.impl;

import com.example.identityService.dto.response.ProductImageResponse;
import com.example.identityService.entity.Product;
import com.example.identityService.entity.ProductImage;
import com.example.identityService.repository.ProductImageRepository;
import com.example.identityService.service.ProductImageService;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProductImageServiceImpl implements ProductImageService {
    ProductImageRepository productImageRepository;

    @Override
    @Cacheable(value = "productImages", key = "#productId")
    public List<ProductImage> getAllByproductId(String productId) {
        return productImageRepository.findAllByProduct_Id(productId);
    }

    @Override
    public ProductImage create(MultipartFile image, Product product) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();
        String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();

        Storage storage = StorageClient.getInstance().bucket().getStorage();
        BlobId blobId = BlobId.of("fir-23945.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(image.getContentType())
                .setAcl(new ArrayList<>(Collections.singletonList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))))
                .build();

        Blob blob = storage.create(blobInfo, image.getBytes());

        // Trả về URL xem ảnh thay vì đường dẫn tải xuống
        return ProductImage.builder()
                .linkImage(String.format("https://storage.googleapis.com/%s/%s", bucket.getName(), fileName))
                .product(product)
                .build();
    }


    @Override
    @Cacheable(value = "productImage", key = "#productImageId")
    public ProductImage getById(String productImageId) {
        return productImageRepository.findById(productImageId).orElseThrow(() -> new RuntimeException("ProductImage not found!"));
    }

    @Override
    @CachePut(value = "productImage", key = "#productImage.id")
    public ProductImage update(ProductImage productImage) {
        return productImageRepository.save(productImage);
    }

    @Override
    @CacheEvict(value = "productImage", key = "#productImageId")
    public String delete(String productImageId) {
        productImageRepository.deleteById(productImageId);
        return "Delete successfully";
    }
}
