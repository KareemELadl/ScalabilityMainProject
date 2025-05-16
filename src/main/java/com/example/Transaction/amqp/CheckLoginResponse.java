package com.example.Transaction.amqp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CheckLoginResponse {
    private boolean valid;
    private String userId;

    public CheckLoginResponse() {}
    public CheckLoginResponse(boolean valid, String userId) {
        this.valid = valid;
        this.userId = userId;
    }

}