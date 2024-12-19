package com.store.store.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private String imageUrl;

    // Menggunakan enum sebagai tipe data biasa, bukan asosiasi relasi
    @Enumerated(EnumType.STRING)  // Menyimpan enum sebagai string di database
    @Column(nullable = false)
    private ProductCategory category;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)  // Relasi ke User (penjual)
    private User seller;

    // Menambahkan variabel active
    @Column(nullable = false)
    private boolean active = true;

    // Constructor default
    public Product() {}

    // Constructor dengan parameter id
    public Product(Long id) {
        this.id = id;
    }

    // Getter dan Setter untuk seller
    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
