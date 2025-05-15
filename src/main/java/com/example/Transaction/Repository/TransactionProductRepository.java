package com.example.Transaction.Repository;

import com.example.Transaction.Model.TransactionProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionProductRepository extends JpaRepository<TransactionProduct, Long> {
    List<TransactionProduct> findByTransactionId(UUID transactionId);
}