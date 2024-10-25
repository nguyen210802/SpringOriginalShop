package com.example.identityService.repository;

import com.example.identityService.entity.ProductImage;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, String> {
    @EntityGraph(attributePaths = {"image"})
    List<ProductImage> findAllByProduct_Id(String productId);
}
