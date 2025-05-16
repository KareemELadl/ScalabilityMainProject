package com.example.Transaction.Service;


import com.example.Transaction.Model.Transaction;
import com.example.Transaction.Model.TransactionProduct;
import com.example.Transaction.Repository.TransactionProductRepository;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ReceiptService {
    private final TemplateEngine templateEngine;
    private final TransactionProductRepository transactionProductRepository;

    public ReceiptService(TemplateEngine templateEngine,
                          TransactionProductRepository transactionProductRepository) {
        this.templateEngine = templateEngine;
        this.transactionProductRepository = transactionProductRepository;
    }


    public String generateHtmlReceipt(Transaction txn) {
        Context ctx = new Context();
        ctx.setVariable("transaction", txn);

        List<TransactionProduct> productLinks =
                transactionProductRepository.findByTransactionId(txn.getId());

        List<String> itemDescriptions = productLinks.stream()
                .map(tp -> tp.getProduct().getName()
                        + " ($" + tp.getProduct().getPrice() + ")")
                .collect(Collectors.toList());
        ctx.setVariable("items", itemDescriptions);

        ctx.setVariable("total", txn.getTotalAmount());
        ctx.setVariable("status", txn.getStatus());

        return templateEngine.process("receipt", ctx);
    }
}
