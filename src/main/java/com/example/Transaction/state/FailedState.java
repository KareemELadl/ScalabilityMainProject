// com/example/Transaction/state/FailedState.java
package com.example.Transaction.state;

import com.example.Transaction.Model.Transaction;
import com.example.Transaction.event.TransactionFailedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component("FAILED")
@RequiredArgsConstructor
public class FailedState implements TransactionState {
    private final ApplicationEventPublisher publisher;

    @Override
    public void onEnter(Transaction txn) {
        publisher.publishEvent(new TransactionFailedEvent(txn, null));

    }
}
