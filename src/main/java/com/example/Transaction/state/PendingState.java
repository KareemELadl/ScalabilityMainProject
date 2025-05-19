package com.example.Transaction.state;

import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Service.TokenClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component("PENDING")
public class PendingState implements TransactionState {


    @Autowired
    private TokenClient tokenClient;

    @Override
    public void onEnter(Transaction txn) {
        Instant now = Instant.now();
        txn.setCreatedAt(now);
        txn.setUpdatedAt(now);
//        txn.setCustomerEmail(tokenClient.getEmailFromToken());
//
    }
}
