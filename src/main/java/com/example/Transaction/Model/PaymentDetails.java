package com.example.Transaction.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payment_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PaymentDetails {
    @Id
    @Column(name = "transaction_id", columnDefinition = "uuid")
    private UUID transactionId;

    @Column(nullable = false)
    private String provider;

    @Column(name = "status_code", nullable = false)
    private String statusCode;

    @Column(name = "paid_at")
    private Instant paidAt;

    @Column(name = "raw_response", columnDefinition = "TEXT")
    private String rawResponse;
}

