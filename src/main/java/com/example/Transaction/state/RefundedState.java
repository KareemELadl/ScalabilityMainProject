package com.example.Transaction.state;

import com.example.Transaction.Model.Transaction;
import org.springframework.stereotype.Component;


@Component("REFUNDED")
public class RefundedState implements TransactionState {
    @Override
    public void onEnter(Transaction transaction) {
        System.out.println("Entering REFUNDED state for Txn: " + transaction.getId());
    }
}
