package com.example.Transaction.event;

import com.example.Transaction.Model.Transaction;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class TransactionRefundedEvent extends ApplicationEvent {
    private final Transaction transaction;

    public TransactionRefundedEvent(Transaction transaction) {
        super(transaction);
        this.transaction = transaction;
    }
}
