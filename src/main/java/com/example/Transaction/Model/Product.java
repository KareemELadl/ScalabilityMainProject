package com.example.Transaction.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id @UuidGenerator
    @Column(nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    private Long merchantId;

    @Column(length = 150, nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("prod-txps")
    private List<TransactionProduct> transactions = new ArrayList<>();
}
