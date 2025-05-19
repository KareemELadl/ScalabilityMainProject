package com.example.Transaction.Service;

import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Model.TransactionStatus;
import com.example.Transaction.Repository.ProductRepository;
import com.example.Transaction.Repository.TransactionProductRepository;
import com.example.Transaction.Repository.TransactionRepository;
import com.example.Transaction.command.*;
import com.example.Transaction.state.TransactionStateContext;
import com.example.Transaction.strategy.PaymentStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repo;
    private final PaymentStrategyFactory strategyFactory;
    private final TransactionStateContext stateContext;
    private final TransactionProductRepository transactionProductRepo;
    private final ProductRepository productRepo;




    @Autowired
    public TransactionServiceImpl(TransactionRepository repo,
                                  PaymentStrategyFactory strategyFactory,
                                  TransactionStateContext stateContext, TransactionProductRepository transactionProductRepo, ProductRepository productRepo) {
        this.repo = repo;
        this.strategyFactory = strategyFactory;
        this.stateContext = stateContext;
        this.transactionProductRepo = transactionProductRepo;
        this.productRepo = productRepo;
    }

    @Override
    @Transactional
    public Transaction createTransaction(Transaction txn, String provider) throws Exception {

        TransactionCommand cmd = CommandFactory.build(
                "create",
                txn,
                provider,
                repo,
                productRepo,
                strategyFactory,
                stateContext
        );

        return cmd.execute();
    }


    @Override
    public Transaction getTransaction(Long transactionId) {
        return repo.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return repo.findAll();
    }


    @Override
    public List<Transaction> getUserTransactions(Long userId) {
        return repo.findByUserIdOrderByCreatedAtDesc(userId);
    }


    @Override
    @Transactional
    public Transaction updateStatus(Long transactionId, TransactionStatus status) throws Exception {
        TransactionCommand cmd = CommandFactory.build(
                "create",
                repo,
                stateContext,
                transactionId,
                status);
        return cmd.execute();
    }


    @Override
    @Transactional
    public void deleteTransaction(Long transactionId) throws Exception {
        TransactionCommand cmd = CommandFactory.build(
                "delete",repo, transactionId);
        cmd.execute();
    }


    @Override
    public List<Transaction> filterByDate(Long userID, Date start, Date end) {
        List<Transaction> transactions = getUserTransactions(userID);
        if (transactions.isEmpty())
            throw new IllegalArgumentException("This User does not have any Transactions");

        Instant startInstant = start.toInstant();
        Instant endInstant = end.toInstant();

        transactions.removeIf(transaction ->
                transaction.getCreatedAt().isBefore(startInstant) ||
                        transaction.getCreatedAt().isAfter(endInstant)
        );

        return transactions;
    }


}
