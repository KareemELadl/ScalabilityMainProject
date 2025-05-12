package com.example.Transaction.state;

import com.example.Transaction.Model.Transaction;
import org.springframework.stereotype.Component;

@Component("COMPLETED")
public class CompletedState implements TransactionState {
    @Override
    public void onEnter(Transaction transaction) {
        System.out.println("Entering COMPLETED state for Txn: " + transaction.getId());
    }
}
