// com/example/Transaction/state/CompletedState.java
package com.example.Transaction.state;

import com.example.Transaction.Model.Transaction;
import com.example.Transaction.event.TransactionCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component("COMPLETED")
@RequiredArgsConstructor
public class CompletedState implements TransactionState {
    private final ApplicationEventPublisher publisher;

    @Override
    public void onEnter(Transaction txn) {

        publisher.publishEvent(new TransactionCreatedEvent(txn));
        System.out.println("Txn " + txn.getId() + " → COMPLETED (event published)");
    }
}
