package com.example.Transaction.amqp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckLoginRequest {
    private String token;

    public CheckLoginRequest() {}
    public CheckLoginRequest(String token) { this.token = token; }

}