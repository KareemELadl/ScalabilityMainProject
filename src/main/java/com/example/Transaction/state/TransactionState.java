package com.example.Transaction.state;

import com.example.Transaction.Model.Transaction;

public interface TransactionState {

    void onEnter(Transaction transaction);


    default void onExit(Transaction transaction) {
    }
}