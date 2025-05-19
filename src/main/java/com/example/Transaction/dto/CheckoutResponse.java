package com.example.Transaction.dto;

import com.example.Transaction.Model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutResponse {
    private Transaction transaction;
    private String sessionUrl;    // null unless provider == "visa"
    private String sessionId;     // optional extra
}