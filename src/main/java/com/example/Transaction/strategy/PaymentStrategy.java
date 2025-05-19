package com.example.Transaction.strategy;

import com.example.Transaction.dto.CheckoutResponse;
import com.example.Transaction.Model.Transaction;

public interface PaymentStrategy {
    CheckoutResponse charge(Transaction txn) throws Exception;
}