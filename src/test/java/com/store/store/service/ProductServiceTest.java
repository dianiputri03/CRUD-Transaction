package com.store.store.service;

import com.store.store.DTO.ProductDto;
import com.store.store.model.Product;
import com.store.store.model.ProductCategory;
import com.store.store.model.User;
import com.store.store.repository.ProductRepository;
import com.store.store.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class ProductServiceTest {

    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final ProductService productService = new ProductService(productRepository, userRepository);

    @Test
    void testCreateProduct() {
        // Arrange
        User seller = new User();
        seller.setUsername("seller123");

        ProductDto productDto = new ProductDto();
        productDto.setName("Product A");
        productDto.setDescription("Description of Product A");
        productDto.setPrice(BigDecimal.valueOf(100.0));
        productDto.setStock(10);
        productDto.setCategory(ProductCategory.ELECTRONICS);
        productDto.setImageUrl("http://example.com/image.jpg");

        Product savedProduct = new Product();
        savedProduct.setId(1L);
        savedProduct.setName(productDto.getName());
        savedProduct.setDescription(productDto.getDescription());
        savedProduct.setPrice(productDto.getPrice());
        savedProduct.setStock(productDto.getStock());
        savedProduct.setCategory(productDto.getCategory());
        savedProduct.setImageUrl(productDto.getImageUrl());
        savedProduct.setSeller(seller);

        Mockito.when(userRepository.findByUsername("seller123")).thenReturn(Optional.of(seller));
        Mockito.when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // Act
        Product result = productService.createProduct(productDto, "seller123");

        // Assert
        assertNotNull(result);
        assertEquals("Product A", result.getName());
        assertEquals("Description of Product A", result.getDescription());
        assertEquals(BigDecimal.valueOf(100.0), result.getPrice());
        assertEquals(10, result.getStock());
        assertEquals(ProductCategory.ELECTRONICS, result.getCategory());
        assertEquals("http://example.com/image.jpg", result.getImageUrl());
    }

    @Test
    void testGetAllProducts() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product A");

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product B");

        List<Product> products = Arrays.asList(product1, product2);

        Mockito.when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product A", result.get(0).getName());
    }

    @Test
    void testGetProductById() {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("Product A");

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act
        Product result = productService.getProductById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Product A", result.getName());
    }

    @Test
    void testUpdateProduct() {
        // Arrange
        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setName("Old Product");

        ProductDto productDto = new ProductDto();
        productDto.setName("Updated Product");
        productDto.setDescription("Updated Description");
        productDto.setPrice(BigDecimal.valueOf(150.0));
        productDto.setStock(20);
        productDto.setCategory(ProductCategory.ELECTRONICS);
        productDto.setImageUrl("http://example.com/updated-image.jpg");

        Product updatedProduct = new Product();
        updatedProduct.setId(1L);
        updatedProduct.setName(productDto.getName());
        updatedProduct.setDescription(productDto.getDescription());
        updatedProduct.setPrice(productDto.getPrice());
        updatedProduct.setStock(productDto.getStock());
        updatedProduct.setCategory(productDto.getCategory());
        updatedProduct.setImageUrl(productDto.getImageUrl());

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        Mockito.when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // Act
        Product result = productService.updateProduct(1L, productDto);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Product", result.getName());
        assertEquals("Updated Description", result.getDescription());
    }

    @Test
    void testDeleteProduct() {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("Product A");

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Mockito.doNothing().when(productRepository).delete(product);

        // Act
        productService.deleteProduct(1L);

        // Assert
        Mockito.verify(productRepository, Mockito.times(1)).delete(product);
    }

    @Test
    void testGetProductsByCategory() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1L);
        product1.setCategory(ProductCategory.ELECTRONICS);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setCategory(ProductCategory.ELECTRONICS);

        List<Product> products = Arrays.asList(product1, product2);

        Mockito.when(productRepository.findByCategory(ProductCategory.ELECTRONICS)).thenReturn(products);

        // Act
        List<Product> result = productService.getProductsByCategory(ProductCategory.ELECTRONICS);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetProductsByPriceRange() {
        // Arrange
        Product product1 = new Product();
        product1.setId(1L);
        product1.setPrice(BigDecimal.valueOf(100));

        Product product2 = new Product();
        product2.setId(2L);
        product2.setPrice(BigDecimal.valueOf(200));

        List<Product> products = Arrays.asList(product1, product2);

        Mockito.when(productRepository.findProductsByPriceRange(BigDecimal.valueOf(50), BigDecimal.valueOf(250))).thenReturn(products);

        // Act
        List<Product> result = productService.getProductsByPriceRange(BigDecimal.valueOf(50), BigDecimal.valueOf(250));

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
