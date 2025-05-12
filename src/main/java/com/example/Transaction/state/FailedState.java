package com.example.Transaction.state;

import com.example.Transaction.Model.Transaction;
import org.springframework.stereotype.Component;


@Component("FAILED")
public class FailedState implements TransactionState {
    @Override
    public void onEnter(Transaction transaction) {
        System.out.println("Entering FAILED state for Txn: " + transaction.getId());
    }
}
