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

    Transaction getTransaction(Long transactionId);

    List<Transaction> getAllTransactions();

    List<Transaction> getUserTransactions(Long userId);
    Transaction updateStatus(Long transactionId, TransactionStatus status) throws Exception;
    void deleteTransaction(Long transactionId) throws Exception;


    List<Transaction> filterByDate(Long userID, Date start, Date end);

}
