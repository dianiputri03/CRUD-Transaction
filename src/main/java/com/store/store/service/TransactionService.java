package com.store.store.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.store.store.DTO.TransactionDto;
import com.store.store.model.Product;
import com.store.store.model.Transaction;
import com.store.store.model.User;
import com.store.store.repository.ProductRepository;
import com.store.store.repository.TransactionRepository;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final ProductRepository productRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, ProductRepository productRepository) {
        this.transactionRepository = transactionRepository;
        this.productRepository = productRepository;
    }

    public List<TransactionDto> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public TransactionDto createTransaction(TransactionDto transactionDto) {
        // Ambil produk berdasarkan productId
        Product product = productRepository.findById(transactionDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + transactionDto.getProductId()));

        // Ubah quantity menjadi BigDecimal dan hitung total price
        BigDecimal quantity = new BigDecimal(transactionDto.getQuantity());
        BigDecimal totalPrice = product.getPrice().multiply(quantity);  // Perkalian BigDecimal

        // Buat transaksi baru
        Transaction transaction = new Transaction();
        transaction.setUser(new User(transactionDto.getUserId()));
        transaction.setProduct(product);
        transaction.setQuantity(transactionDto.getQuantity()); // Simpan quantity dalam Integer
        transaction.setTotalPrice(totalPrice.doubleValue());  // Simpan totalPrice sebagai Double
        transaction.setCreatedAt(LocalDateTime.now());

        Transaction savedTransaction = transactionRepository.save(transaction);
        return mapToDTO(savedTransaction);
    }

    public TransactionDto getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + id));
        return mapToDTO(transaction);
    }

    public TransactionDto updateTransaction(Long id, TransactionDto transactionDto) {
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + id));

        // Ambil produk terbaru dari database
        Product product = productRepository.findById(transactionDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + transactionDto.getProductId()));

        // Update transaksi
        existingTransaction.setUser(new User(transactionDto.getUserId()));
        existingTransaction.setProduct(product);
        existingTransaction.setQuantity(transactionDto.getQuantity());

        // Hitung ulang total price
        BigDecimal quantity = BigDecimal.valueOf(transactionDto.getQuantity());
        BigDecimal totalPrice = product.getPrice().multiply(quantity);
        existingTransaction.setTotalPrice(totalPrice.doubleValue());
        existingTransaction.setTotalPrice(totalPrice.doubleValue());

        existingTransaction.setCreatedAt(LocalDateTime.now());

        // Simpan transaksi yang telah di-update
        Transaction updatedTransaction = transactionRepository.save(existingTransaction);
        return mapToDTO(updatedTransaction);
    }

    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with ID: " + id));
        transactionRepository.delete(transaction);
    }

    private TransactionDto mapToDTO(Transaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setUserId(transaction.getUser().getId());
        dto.setProductId(transaction.getProduct().getId());
        dto.setQuantity(transaction.getQuantity());
        dto.setTotalPrice(transaction.getTotalPrice());
        dto.setCreatedAt(transaction.getCreatedAt());
        return dto;
    }}

