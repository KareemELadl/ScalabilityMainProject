package com.example.Transaction.Service;

import com.example.Transaction.Model.Transaction;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class ReceiptService {
    private final TemplateEngine templateEngine;

    public ReceiptService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }


    public String generateHtmlReceipt(Transaction txn) {
        Context ctx = new Context();
        ctx.setVariable("transaction", txn);
        return templateEngine.process("receipt", ctx);
    }
}