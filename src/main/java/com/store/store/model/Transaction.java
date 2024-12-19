package com.store.store.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer quantity;
// Tambahkan quantity
@Column(nullable = false, columnDefinition = "DOUBLE PRECISION DEFAULT 0.0")
private Double totalPrice;


   // Tambahkan total price

    @Column(nullable = false)
    private LocalDateTime createdAt;
}