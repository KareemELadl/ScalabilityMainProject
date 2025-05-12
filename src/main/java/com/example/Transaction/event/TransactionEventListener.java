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

    public TransactionEventListener(EmailService emailService, ReceiptService receiptService) {
        this.emailService = emailService;
        this.receiptService = receiptService;
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
    }
    @EventListener
    public void handleTransactionRefunded(TransactionRefundedEvent event) {
        Transaction txn = event.getTransaction();
        String html = receiptService.generateHtmlReceipt(txn);
        emailService.sendHtmlEmail(
                txn.getCustomerEmail(),
                "Your Refund Receipt",
                html
        );
    }

}
