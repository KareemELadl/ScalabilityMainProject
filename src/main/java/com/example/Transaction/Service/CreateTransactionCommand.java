package com.example.Transaction.command;

import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Model.TransactionProduct;
import com.example.Transaction.Model.TransactionStatus;
import com.example.Transaction.Model.Product;
import com.example.Transaction.Repository.ProductRepository;
import com.example.Transaction.Repository.TransactionRepository;
import com.example.Transaction.state.TransactionStateContext;
import com.example.Transaction.strategy.PaymentStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.Instant;

@RequiredArgsConstructor
public class CreateTransactionCommand implements TransactionCommand {

    private final Transaction txn;
    private final String provider;
    private final TransactionRepository txnRepo;
    private final ProductRepository productRepo;
    private final PaymentStrategyFactory strategyFactory;
    private final TransactionStateContext stateCtx;

    @Override
    @Transactional
    public Transaction execute() throws Exception {
        stateCtx.transition(txn, TransactionStatus.PENDING);
        assignProductsAndValidateStock();
        calculateAndSetTotalAmount();
        txnRepo.save(txn);

        try {
            processPayment();
            completeTransaction();
            return txn;
        } catch (Exception ex) {
            failTransaction(ex);
            throw ex;
        }
    }


    private void assignProductsAndValidateStock() {
        for (TransactionProduct item : txn.getItems()) {
            item.setTransaction(txn);
            Long prodId = (item.getProduct() != null && item.getProduct().getId() != null)
                    ? item.getProduct().getId()
                    : item.getProductId();
            if (prodId == null) {
                throw new IllegalArgumentException("Each item needs a productId");
            }

            Product product = productRepo.findById(prodId).orElseThrow(() ->
                    new IllegalArgumentException("Product not found: " + prodId));

            int available = product.getStock();
            int requested  = item.getQuantity();
            if (requested > available) {
                throw new IllegalStateException("Insufficient stock for product " + prodId +
                        ": requested " + requested + ", available " + available);
            }

            product.setStock(available - requested);
            productRepo.save(product);

            item.setProduct(product);
            item.setProductId(product.getId());
        }
    }
    private void calculateAndSetTotalAmount() {
        BigDecimal total = txn.getItems().stream()
                .map(i -> i.getProduct().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        txn.setTotalAmount(total);
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
    }
}
