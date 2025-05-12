package com.example.Transaction.strategy;


import com.example.Transaction.Model.Transaction;

public interface PaymentStrategy {
    void charge(Transaction transaction);
}