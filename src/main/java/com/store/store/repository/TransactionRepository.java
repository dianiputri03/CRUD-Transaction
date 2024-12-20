package com.store.store.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.store.store.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Add custom query methods here if needed
}