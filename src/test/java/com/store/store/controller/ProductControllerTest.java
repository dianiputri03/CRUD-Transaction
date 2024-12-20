package com.store.store.controller;

import com.store.store.DTO.ProductDto;
import com.store.store.model.Product;
import com.store.store.model.ProductCategory;
import com.store.store.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductDto productDto;
    private Product product;
    private final String SELLER_USERNAME = "seller1";
    private final Long PRODUCT_ID = 1L;

    @BeforeEach
    void setUp() {
        // Arrange - Common setup for all tests
        productDto = new ProductDto();
        productDto.setName("Test Product");
        productDto.setDescription("Test Description");
        productDto.setPrice(new BigDecimal("99.99"));
        productDto.setCategory(ProductCategory.ELECTRONICS);
        productDto.setStock(10);

        product = new Product();
        product.setId(PRODUCT_ID);
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal("99.99"));
        product.setCategory(ProductCategory.ELECTRONICS);
        product.setStock(10);
    }

    @Test
    void createProduct_ValidProduct_ReturnsCreatedProduct() {
        // Arrange
        when(productService.createProduct(any(ProductDto.class), eq(SELLER_USERNAME)))
                .thenReturn(product);

        // Act
        ResponseEntity<Product> response = productController.createProduct(productDto, SELLER_USERNAME);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(PRODUCT_ID, response.getBody().getId());
        verify(productService).createProduct(productDto, SELLER_USERNAME);
    }

    @Test
    void getAllProducts_ReturnsListOfProducts() {
        // Arrange
        List<Product> products = Arrays.asList(product);
        when(productService.getAllProducts()).thenReturn(products);

        // Act
        ResponseEntity<List<Product>> response = productController.getAllProducts();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(productService).getAllProducts();
    }

    @Test
    void getProductById_ExistingId_ReturnsProduct() {
        // Arrange
        when(productService.getProductById(PRODUCT_ID)).thenReturn(product);

        // Act
        ResponseEntity<Product> response = productController.getProductById(PRODUCT_ID);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(PRODUCT_ID, response.getBody().getId());
        verify(productService).getProductById(PRODUCT_ID);
    }

    @Test
    void updateProduct_ValidUpdate_ReturnsUpdatedProduct() {
        // Arrange
        when(productService.updateProduct(eq(PRODUCT_ID), any(ProductDto.class)))
                .thenReturn(product);

        // Act
        ResponseEntity<Product> response = productController.updateProduct(PRODUCT_ID, productDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(PRODUCT_ID, response.getBody().getId());
        verify(productService).updateProduct(PRODUCT_ID, productDto);
    }

    @Test
    void deleteProduct_ExistingId_ReturnsNoContent() {
        // Arrange
        doNothing().when(productService).deleteProduct(PRODUCT_ID);

        // Act
        ResponseEntity<Void> response = productController.deleteProduct(PRODUCT_ID);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productService).deleteProduct(PRODUCT_ID);
    }

    @Test
    void getProductsByCategory_ValidCategory_ReturnsFilteredProducts() {
        // Arrange
        List<Product> products = Arrays.asList(product);
        when(productService.getProductsByCategory(ProductCategory.ELECTRONICS))
                .thenReturn(products);

        // Act
        ResponseEntity<List<Product>> response = productController.getProductsByCategory(ProductCategory.ELECTRONICS);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(ProductCategory.ELECTRONICS, response.getBody().get(0).getCategory());
        verify(productService).getProductsByCategory(ProductCategory.ELECTRONICS);
    }

    @Test
    void getProductsByPriceRange_ValidRange_ReturnsFilteredProducts() {
        // Arrange
        BigDecimal minPrice = new BigDecimal("50.00");
        BigDecimal maxPrice = new BigDecimal("150.00");
        List<Product> products = Arrays.asList(product);
        when(productService.getProductsByPriceRange(minPrice, maxPrice))
                .thenReturn(products);

        // Act
        ResponseEntity<List<Product>> response = productController.getProductsByPriceRange(minPrice, maxPrice);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().get(0).getPrice().compareTo(minPrice) >= 0);
        assertTrue(response.getBody().get(0).getPrice().compareTo(maxPrice) <= 0);
        verify(productService).getProductsByPriceRange(minPrice, maxPrice);
    }
}