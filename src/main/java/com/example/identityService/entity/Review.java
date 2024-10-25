package com.example.identityService.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String title;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @ManyToOne
    @JoinColumn(name = "reviewer_id", nullable = false)
    User reviewer;

    int rate;

    @OneToOne
    @JoinColumn(name = "comment_id", nullable = false)
    Comment comment;

    LocalDate createAt;
    LocalDate updateAt;

    boolean deleted;

    @PrePersist
    private void setCreateAt() {
        this.createAt = LocalDate.now();
    }

    @PreUpdate
    private void setUpdateAt() {
        this.updateAt = LocalDate.now();
    }
}
