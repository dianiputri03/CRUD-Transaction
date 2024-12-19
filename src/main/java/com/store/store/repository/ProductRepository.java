package com.store.store.repository;


import com.store.store.model.Product;
import com.store.store.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.math.BigDecimal;  // tambahkan baris ini


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(ProductCategory category);
    List<Product> findBySellerUsername(String username);

    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
}