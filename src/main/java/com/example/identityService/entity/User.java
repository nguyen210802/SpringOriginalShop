package com.example.identityService.entity;

import com.example.identityService.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(unique = true, nullable = false)
    String username;

    @Column(nullable = false)
    String password;

    @Column(unique = true, nullable = false)
    String email;

    @Enumerated(EnumType.STRING)
    Role role;

    @Column(unique = true, nullable = false)
    String phone;

    LocalDate dob;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    List<Address> address;

    @OneToOne(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    Cart cart;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    List<Product> products;

    @OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    List<Review> reviews;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    List<Order> orders;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    List<Notification> notifications;

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
