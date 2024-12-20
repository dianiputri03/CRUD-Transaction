package com.store.store.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.store.store.DTO.TransactionDto;
import com.store.store.model.Product;
import com.store.store.model.Transaction;
import com.store.store.model.User;
import com.store.store.repository.ProductRepository;
import com.store.store.repository.TransactionRepository;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction mockTransaction;
    private Product mockProduct;
    private User mockUser;
    private TransactionDto mockTransactionDto;

    @BeforeEach
    void setUp() {
        // Setup mock objects
        mockUser = new User(1L);
        mockProduct = new Product();
        mockProduct.setId(1L);
        mockProduct.setPrice(new BigDecimal("100.00"));

        mockTransaction = new Transaction();
        mockTransaction.setId(1L);
        mockTransaction.setUser(mockUser);
        mockTransaction.setProduct(mockProduct);
        mockTransaction.setQuantity(2);
        mockTransaction.setTotalPrice(200.00);
        mockTransaction.setCreatedAt(LocalDateTime.now());

        mockTransactionDto = new TransactionDto();
        mockTransactionDto.setUserId(1L);
        mockTransactionDto.setProductId(1L);
        mockTransactionDto.setQuantity(2);
    }

    @Test
    void getAllTransactions_ShouldReturnListOfTransactionDto() {
        // Arrange
        List<Transaction> transactions = Arrays.asList(mockTransaction);
        when(transactionRepository.findAll()).thenReturn(transactions);

        // Act
        List<TransactionDto> result = transactionService.getAllTransactions();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockTransaction.getId(), result.get(0).getId());
        verify(transactionRepository).findAll();
    }

    @Test
    void createTransaction_ShouldReturnNewTransactionDto() {
        // Arrange
        when(productRepository.findById(mockTransactionDto.getProductId()))
                .thenReturn(Optional.of(mockProduct));
        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(mockTransaction);

        // Act
        TransactionDto result = transactionService.createTransaction(mockTransactionDto);

        // Assert
        assertNotNull(result);
        assertEquals(mockTransaction.getId(), result.getId());
        assertEquals(mockTransaction.getTotalPrice(), result.getTotalPrice());
        verify(productRepository).findById(mockTransactionDto.getProductId());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void getTransactionById_ShouldReturnTransactionDto() {
        // Arrange
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(mockTransaction));

        // Act
        TransactionDto result = transactionService.getTransactionById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(mockTransaction.getId(), result.getId());
        verify(transactionRepository).findById(1L);
    }

    @Test
    void updateTransaction_ShouldReturnUpdatedTransactionDto() {
        // Arrange
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(mockTransaction));
        when(productRepository.findById(1L)).thenReturn(Optional.of(mockProduct));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(mockTransaction);

        // Act
        TransactionDto result = transactionService.updateTransaction(1L, mockTransactionDto);

        // Assert
        assertNotNull(result);
        assertEquals(mockTransaction.getId(), result.getId());
        assertEquals(mockTransaction.getTotalPrice(), result.getTotalPrice());
        verify(transactionRepository).findById(1L);
        verify(productRepository).findById(1L);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void deleteTransaction_ShouldDeleteSuccessfully() {
        // Arrange
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(mockTransaction));
        doNothing().when(transactionRepository).delete(mockTransaction);

        // Act
        transactionService.deleteTransaction(1L);

        // Assert
        verify(transactionRepository).findById(1L);
        verify(transactionRepository).delete(mockTransaction);
    }

    @Test
    void getTransactionById_ShouldThrowException_WhenTransactionNotFound() {
        // Arrange
        when(transactionRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            transactionService.getTransactionById(999L);
        });
        verify(transactionRepository).findById(999L);
    }

    @Test
    void createTransaction_ShouldThrowException_WhenProductNotFound() {
        // Arrange
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            transactionService.createTransaction(mockTransactionDto);
        });
        verify(productRepository).findById(any());
    }
}