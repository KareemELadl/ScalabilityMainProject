package com.example.Transaction.Service;

import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Model.TransactionProduct;
import com.example.Transaction.Model.TransactionStatus;
import com.example.Transaction.Repository.ProductRepository;
import com.example.Transaction.Repository.TransactionProductRepository;
import com.example.Transaction.Repository.TransactionRepository;
import com.example.Transaction.command.CreateTransactionCommand;
import com.example.Transaction.command.DeleteTransactionCommand;
import com.example.Transaction.command.TransactionCommand;
import com.example.Transaction.command.UpdateTransactionStatusCommand;
import com.example.Transaction.state.TransactionStateContext;
import com.example.Transaction.strategy.PaymentStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repo;
    private final PaymentStrategyFactory strategyFactory;
    private final ApplicationEventPublisher publisher;
    private final TransactionStateContext stateContext;
    private final TransactionProductRepository transactionProductRepo;
    private final ProductRepository productRepo;



    @Autowired
    public TransactionServiceImpl(TransactionRepository repo,
                                  PaymentStrategyFactory strategyFactory,
                                  ApplicationEventPublisher publisher,
                                  TransactionStateContext stateContext, TransactionProductRepository transactionProductRepo, ProductRepository productRepo) {
        this.repo = repo;
        this.strategyFactory = strategyFactory;
        this.publisher = publisher;
        this.stateContext = stateContext;
        this.transactionProductRepo = transactionProductRepo;
        this.productRepo = productRepo;
    }

    @Override
    @Transactional
    public Transaction createTransaction(Transaction txn, String provider) throws Exception {

        TransactionCommand cmd = new CreateTransactionCommand(
                txn,
                provider,
                repo,
                productRepo,
                strategyFactory,
                stateContext,
                publisher
        );

        return cmd.execute();
    }


    @Override
    public Transaction getTransaction(UUID transactionId) {
        return repo.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return repo.findAll();
    }


    @Override
    public List<Transaction> getUserTransactions(UUID userId) {
        return repo.findByUserIdOrderByCreatedAtDesc(userId);
    }


    @Override
    @Transactional
    public Transaction updateStatus(UUID transactionId, TransactionStatus status) throws Exception {
        TransactionCommand cmd = new UpdateTransactionStatusCommand(
                repo,
                stateContext,
                transactionId,
                status);
        return cmd.execute();
    }


    @Override
    @Transactional
    public void deleteTransaction(UUID transactionId) throws Exception {
        TransactionCommand cmd = new DeleteTransactionCommand(repo, transactionId);
        cmd.execute();
    }


    @Override
    public List<Transaction> filterByDate(UUID userID, Date start, Date end) {
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
