package com.example.Transaction.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockConsumedEvent implements Serializable {
    private Long orderId;
    private Long productId;
    private Long merchantId;
    private Integer quantity; // minimum: 1
    private LocalDateTime createdAt;
}