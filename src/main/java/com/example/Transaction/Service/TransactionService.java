package com.example.Transaction.Service;

import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Model.TransactionProduct;
import com.example.Transaction.Model.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface TransactionService {

    @Transactional
    Transaction createTransaction(Transaction txn, String provider) throws Exception;

    Transaction getTransaction(UUID transactionId);

    List<Transaction> getAllTransactions();

    List<Transaction> getUserTransactions(UUID userId);
    Transaction updateStatus(UUID transactionId, TransactionStatus status) throws Exception;
    void deleteTransaction(UUID transactionId) throws Exception;


    List<Transaction> filterByDate(UUID userID, Date start, Date end);

}
