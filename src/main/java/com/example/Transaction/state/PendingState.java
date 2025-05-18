// com/example/Transaction/state/PendingState.java
package com.example.Transaction.state;

import com.example.Transaction.Model.Transaction;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component("PENDING")
public class PendingState implements TransactionState {
    @Override
    public void onEnter(Transaction txn) {
        Instant now = Instant.now();
        txn.setCreatedAt(now);
        txn.setUpdatedAt(now);

    }
}
