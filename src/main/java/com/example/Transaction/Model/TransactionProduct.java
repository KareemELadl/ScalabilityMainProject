package com.example.Transaction.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;


@Entity @Table(name = "transaction_products")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TransactionProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    @Transient
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonBackReference("prod-txps")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_id", nullable = false)
    @JsonBackReference("txn-items")
    private Transaction transaction;

    @Column(nullable = false)
    private Integer quantity;

}
