package com.example.Transaction.state;

import com.example.Transaction.Model.Transaction;
import org.springframework.stereotype.Component;

@Component("PENDING")
public class PendingState implements TransactionState {
    @Override
    public void onEnter(Transaction transaction) {
        System.out.println("Entering PENDING state for Txn: " + transaction.getId());
    }
}
