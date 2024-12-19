package com.store.store.DTO;

import java.time.LocalDateTime;

public class TransactionDto {
    private Long id;  // ID transaksi
    private Long userId;  // ID pengguna
    private Long productId;  // ID produk
    private Integer quantity;  // Jumlah produk yang dibeli
    private Double totalPrice;  // Total harga
    private LocalDateTime createdAt;  // Waktu transaksi

    // Getter dan Setter untuk id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter dan Setter untuk userId
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // Getter dan Setter untuk productId
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    // Getter dan Setter untuk quantity
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    // Getter dan Setter untuk totalPrice
    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Getter dan Setter untuk createdAt
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
