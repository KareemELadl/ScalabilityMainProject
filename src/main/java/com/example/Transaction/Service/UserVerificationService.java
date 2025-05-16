package com.example.Transaction.Service;

import java.util.UUID;

public interface UserVerificationService {
    UUID verifyLogin(String token);
}
