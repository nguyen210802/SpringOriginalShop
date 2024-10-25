package com.example.identityService.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "buyer_id", nullable = false)
//    @JsonIgnore
    User buyer;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    Address address;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonIgnore
    List<OrderItem> orderItems;

    @Column(nullable = false)
    double totalAmount;

    String status;

    @Column(nullable = false)
    LocalDate createAt;
    LocalDate updateAt;

    @PrePersist
    private void setCreateAt(){
        this.createAt = LocalDate.now();
    }

    @PreUpdate
    private void setUpdateAt(){
        this.updateAt = LocalDate.now();
    }
}
