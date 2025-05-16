package com.example.Transaction.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity @Table(name = "transaction_products")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TransactionProduct {

    @Id @UuidGenerator
    @Column(nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    @Transient
    private UUID productId;

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
