package com.example.Transaction.strategy;

import com.example.Transaction.Model.Transaction;
import org.springframework.stereotype.Component;

@Component("cash")
public class CashPaymentStrategy implements PaymentStrategy{
    @Override
    public void charge(Transaction transaction) {
        System.out.println("Charging transaction " + transaction.getId() + " using Cash.");

    }
}
