package com.example.identityService.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
     String id;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    User seller;

    @Column(nullable = false)
    String name;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    List<ProductImage> images;

    String description;
    String manufacturer;

    @Column(nullable = false)
    double price;

    @Column(nullable = false)
    int remainingQuantity;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    List<Review> reviews;

    @Column(nullable = false)
    LocalDate createAt;
    LocalDate updateAt;

    boolean deleted;

    @PrePersist
    private void setCreateAt(){
        this.createAt = LocalDate.now();
    }

    @PreUpdate
    private void setUpdateAt(){
        this.updateAt = LocalDate.now();
    }
}
