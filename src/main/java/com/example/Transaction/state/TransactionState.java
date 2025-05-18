// com/example/Transaction/state/TransactionState.java
package com.example.Transaction.state;

import com.example.Transaction.Model.Transaction;

public interface TransactionState {
    void onEnter(Transaction txn);
    default void onExit(Transaction txn) { }
}
