package com.example.Transaction.Service;

import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Model.TransactionStatus;
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
import java.util.List;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repo;
    private final PaymentStrategyFactory strategyFactory;
    private final ApplicationEventPublisher publisher;
    private final TransactionStateContext stateContext;

    @Autowired
    public TransactionServiceImpl(TransactionRepository repo,
                                  PaymentStrategyFactory strategyFactory,
                                  ApplicationEventPublisher publisher,
                                  TransactionStateContext stateContext) {
        this.repo = repo;
        this.strategyFactory = strategyFactory;
        this.publisher = publisher;
        this.stateContext = stateContext;
    }

    @Override
    @Transactional
    public Transaction createTransaction(Transaction txn, String provider) {
        TransactionCommand cmd = new CreateTransactionCommand(
                txn,
                provider,
                repo,
                strategyFactory,
                stateContext,
                publisher);
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
    public Transaction updateStatus(UUID transactionId, TransactionStatus status) {
        TransactionCommand cmd = new UpdateTransactionStatusCommand(
                repo,
                stateContext,
                transactionId,
                status);
        return cmd.execute();
    }


    @Override
    @Transactional
    public void deleteTransaction(UUID transactionId) {
        TransactionCommand cmd = new DeleteTransactionCommand(repo, transactionId);
        cmd.execute();
    }
    @Transactional
    @Override
    public Transaction addItems(UUID transactionId, List<String> items) {
        Transaction txn = getTransaction(transactionId);
        txn.getItems().addAll(items);
        return repo.save(txn);
    }

}
