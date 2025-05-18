// com/example/Transaction/state/TransactionStateContext.java
package com.example.Transaction.state;

import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Model.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TransactionStateContext {
    private final Map<String, TransactionState> stateMap;

    @Autowired
    public TransactionStateContext(Map<String, TransactionState> states) {
        this.stateMap = states;
    }

    public void transition(Transaction txn, TransactionStatus newStatus) {
        if (txn.getStatus() != null) {
            TransactionState oldS = stateMap.get(txn.getStatus().name());
            if (oldS != null) oldS.onExit(txn);
        }
        txn.setStatus(newStatus);
        TransactionState newS = stateMap.get(newStatus.name());
        if (newS != null) newS.onEnter(txn);
    }
}
