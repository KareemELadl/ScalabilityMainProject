package com.example.Transaction.Repository;

import com.example.Transaction.Model.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, UUID> {}

