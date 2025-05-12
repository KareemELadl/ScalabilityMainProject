package com.example.Transaction.strategy;

import com.example.Transaction.Model.Transaction;
import org.springframework.stereotype.Component;

@Component("visa")
public class VisaPaymentStrategy implements PaymentStrategy{
    @Override
    public void charge(Transaction transaction) {
        System.out.println("Charging transaction " + transaction.getId() + " using Visa.");
    }
}
