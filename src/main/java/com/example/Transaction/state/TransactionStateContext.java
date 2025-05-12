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
        TransactionStatus oldStatus = txn.getStatus();
        if (oldStatus != null) {
            TransactionState oldState = stateMap.get(oldStatus.name());
            if (oldState != null) {
                oldState.onExit(txn);
            }
        }

        txn.setStatus(newStatus);
        TransactionState newState = stateMap.get(newStatus.name());
        if (newState != null) {
            newState.onEnter(txn);
        }
    }
}