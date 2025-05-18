package com.example.Transaction.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionService {

    private static SessionService instance;

    private final Map<String, Long> sessionStore = new ConcurrentHashMap<>();

    // Private constructor
    private SessionService() {}

    // Static access method
    public static SessionService getInstance() {
        if (instance == null) {
            synchronized (SessionService.class) {
                if (instance == null) {
                    instance = new SessionService();
                }
            }
        }
        return instance;
    }

    public void saveSession(String token, Long userId) {
        sessionStore.put(token, userId);
    }

    public void deleteSession(String token) {
        sessionStore.remove(token);
    }

    public Long getUserIdFromToken(String token) {
        return sessionStore.get(token);
    }
}
