package com.store.store.controller;

import com.store.store.DTO.TransactionDto;
import com.store.store.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class TransactionControllerTest {

    private final TransactionService transactionService = Mockito.mock(TransactionService.class);
    private final TransactionController transactionController = new TransactionController(transactionService);

    @Test
    void testGetAllTransactions() {
        // Arrange
        TransactionDto transaction1 = new TransactionDto();
        transaction1.setId(1L);
        transaction1.setUserId(101L);
        transaction1.setProductId(201L);
        transaction1.setQuantity(2);
        transaction1.setTotalPrice(100.0);
        transaction1.setCreatedAt(LocalDateTime.now());

        TransactionDto transaction2 = new TransactionDto();
        transaction2.setId(2L);
        transaction2.setUserId(102L);
        transaction2.setProductId(202L);
        transaction2.setQuantity(3);
        transaction2.setTotalPrice(200.0);
        transaction2.setCreatedAt(LocalDateTime.now());

        List<TransactionDto> transactions = Arrays.asList(transaction1, transaction2);

        Mockito.when(transactionService.getAllTransactions()).thenReturn(transactions);

        // Act
        ResponseEntity<List<TransactionDto>> response = transactionController.getAllTransactions();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(101L, response.getBody().get(0).getUserId());
    }

    @Test
    void testCreateTransaction() {
        // Arrange
        TransactionDto transaction = new TransactionDto();
        transaction.setId(1L);
        transaction.setUserId(101L);
        transaction.setProductId(201L);
        transaction.setQuantity(2);
        transaction.setTotalPrice(100.0);
        transaction.setCreatedAt(LocalDateTime.now());

        Mockito.when(transactionService.createTransaction(any(TransactionDto.class))).thenReturn(transaction);

        TransactionDto requestTransaction = new TransactionDto();
        requestTransaction.setUserId(101L);
        requestTransaction.setProductId(201L);
        requestTransaction.setQuantity(2);
        requestTransaction.setTotalPrice(100.0);
        requestTransaction.setCreatedAt(LocalDateTime.now());

        // Act
        ResponseEntity<TransactionDto> response = transactionController.createTransaction(requestTransaction);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(101L, response.getBody().getUserId());
    }

    @Test
    void testGetTransactionById() {
        // Arrange
        TransactionDto transaction = new TransactionDto();
        transaction.setId(1L);
        transaction.setUserId(101L);
        transaction.setProductId(201L);
        transaction.setQuantity(2);
        transaction.setTotalPrice(100.0);
        transaction.setCreatedAt(LocalDateTime.now());

        Mockito.when(transactionService.getTransactionById(1L)).thenReturn(transaction);

        // Act
        ResponseEntity<TransactionDto> response = transactionController.getTransactionById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(101L, response.getBody().getUserId());
    }

    @Test
    void testUpdateTransaction() {
        // Arrange
        TransactionDto updatedTransaction = new TransactionDto();
        updatedTransaction.setId(1L);
        updatedTransaction.setUserId(101L);
        updatedTransaction.setProductId(201L);
        updatedTransaction.setQuantity(5);
        updatedTransaction.setTotalPrice(250.0);
        updatedTransaction.setCreatedAt(LocalDateTime.now());

        Mockito.when(transactionService.updateTransaction(eq(1L), any(TransactionDto.class))).thenReturn(updatedTransaction);

        TransactionDto requestTransaction = new TransactionDto();
        requestTransaction.setUserId(101L);
        requestTransaction.setProductId(201L);
        requestTransaction.setQuantity(5);
        requestTransaction.setTotalPrice(250.0);
        requestTransaction.setCreatedAt(LocalDateTime.now());

        // Act
        ResponseEntity<TransactionDto> response = transactionController.updateTransaction(1L, requestTransaction);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(101L, response.getBody().getUserId());
    }

    @Test
    void testDeleteTransaction() {
        // Arrange
        Mockito.doNothing().when(transactionService).deleteTransaction(1L);

        // Act
        ResponseEntity<Void> response = transactionController.deleteTransaction(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
