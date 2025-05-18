package com.example.Transaction.event;

import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Service.EmailService;
import com.example.Transaction.Service.ReceiptService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventListener {

    private final EmailService emailService;
    private final ReceiptService receiptService;
    private final StockEventPublisher stockPublisher;


    public TransactionEventListener(EmailService emailService, ReceiptService receiptService, StockEventPublisher stockPublisher) {
        this.emailService = emailService;
        this.receiptService = receiptService;
        this.stockPublisher = stockPublisher;
    }

    @EventListener
    public void onTransactionCreated(TransactionCreatedEvent ev) {
        Transaction txn = ev.getTransaction();
        String html = receiptService.generateHtmlReceipt(txn);
        String userEmail = txn.getCustomerEmail();
        if (userEmail == null) {
            throw new IllegalArgumentException("Transaction.email is null!");
        }
        emailService.sendHtmlEmail(
                txn.getCustomerEmail(),
                "Your Transaction Receipt",
                html
        );
        stockPublisher.publish(txn.getItems());

    }
    @EventListener
    public void handleTransactionRefunded(TransactionFailedEvent event) {
        Transaction txn = event.getTransaction();
        String html = receiptService.generateHtmlReceipt(txn);
        emailService.sendHtmlEmail(
                txn.getCustomerEmail(),
                "Your Transaction is FAILED",
                html
        );
    }

}
