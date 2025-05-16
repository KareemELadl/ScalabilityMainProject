package com.example.Transaction.command;

import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Model.TransactionProduct;
import com.example.Transaction.Model.TransactionStatus;
import com.example.Transaction.Repository.ProductRepository;
import com.example.Transaction.Repository.TransactionRepository;
import com.example.Transaction.event.TransactionCreatedEvent;
import com.example.Transaction.event.TransactionFailedEvent;
import com.example.Transaction.state.TransactionStateContext;
import com.example.Transaction.strategy.PaymentStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
public class CreateTransactionCommand implements TransactionCommand {

    private final Transaction txn;
    private final String provider;
    private final TransactionRepository txnRepo;
    private final ProductRepository productRepo;
    private final PaymentStrategyFactory strategyFactory;
    private final TransactionStateContext stateCtx;
    private final ApplicationEventPublisher publisher;

    @Override
    @Transactional
    public Transaction execute() throws Exception {
        setInitialTransactionState();
        assignProductsToItems();
        calculateAndSetTotalAmount();
        txnRepo.save(txn);

        try {
            processPayment();
            completeTransaction();
            publisher.publishEvent(new TransactionCreatedEvent(txn));
            return txn;
        } catch (Exception ex) {
            failTransaction(ex);
            throw ex;
        }
    }

    private void setInitialTransactionState() {
        stateCtx.transition(txn, TransactionStatus.PENDING);
        Instant now = Instant.now();
        txn.setCreatedAt(now);
        txn.setUpdatedAt(now);
    }

    private void assignProductsToItems() {
        for (TransactionProduct item : txn.getItems()) {
            item.setTransaction(txn);
            UUID prodId = (item.getProduct() != null && item.getProduct().getId() != null)
                    ? item.getProduct().getId()
                    : item.getProductId();
            if (prodId == null) {
                throw new IllegalArgumentException("Each item needs productId");
            }
            item.setProduct(productRepo.getReferenceById(prodId));
        }
    }

    private void calculateAndSetTotalAmount() {
        BigDecimal grandTotal = txn.getItems().stream()
                .map(i -> i.getProduct().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        txn.setTotalAmount(grandTotal);
    }

    private void processPayment() throws Exception {
        strategyFactory.getStrategy(provider).charge(txn);
    }

    private void completeTransaction() {
        stateCtx.transition(txn, TransactionStatus.COMPLETED);
        txn.setUpdatedAt(Instant.now());
        txnRepo.save(txn);
    }

    private void failTransaction(Exception ex) {
        stateCtx.transition(txn, TransactionStatus.FAILED);
        txn.setUpdatedAt(Instant.now());
        txnRepo.save(txn);
        publisher.publishEvent(new TransactionFailedEvent(txn, ex));
    }
}