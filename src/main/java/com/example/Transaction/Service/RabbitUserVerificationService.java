package com.example.Transaction.Service;

import com.example.Transaction.config.RabbitMQConfig;
import com.example.Transaction.amqp.CheckLoginRequest;
import com.example.Transaction.amqp.CheckLoginResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class RabbitUserVerificationService implements UserVerificationService {
    private final RabbitTemplate rabbitTemplate;

    public RabbitUserVerificationService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public UUID verifyLogin(String token) {
        CheckLoginRequest request = new CheckLoginRequest(token);
        Object reply = rabbitTemplate.convertSendAndReceive(
                RabbitMQConfig.USER_LOGIN_EXCHANGE,
                RabbitMQConfig.USER_LOGIN_ROUTING_KEY,
                request
        );
        if (!(reply instanceof CheckLoginResponse)) {
            throw new IllegalStateException("Invalid response from User microservice");
        }
        CheckLoginResponse resp = (CheckLoginResponse) reply;
        if (!resp.isValid()) {
            throw new IllegalStateException("User not logged in");
        }
        return UUID.fromString(resp.getUserId());
    }
}
