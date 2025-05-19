// src/main/java/com/example/Transaction/command/CommandFactory.java
package com.example.Transaction.command;

import java.lang.reflect.Constructor;
import java.util.Map;

/**
 * Builds TransactionCommand instances by key using reflection.
 */
public class CommandFactory {
    private static final Map<String, String> COMMAND_MAP = Map.of(
            "create",        "com.example.Transaction.command.CreateTransactionCommand",
            "updateStatus",  "com.example.Transaction.command.UpdateTransactionStatusCommand",
            "delete",        "com.example.Transaction.command.DeleteTransactionCommand"
    );

    /**
     * Instantiate a TransactionCommand by key.
     *
     * @param key  one of "create", "updateStatus", "delete"
     * @param args constructor arguments in order
     * @return     a new TransactionCommand instance
     */
    public static TransactionCommand build(String key, Object... args) {
        try {
            String className = COMMAND_MAP.get(key);
            Class<?> cmdClass = Class.forName(className);

            for (Constructor<?> ctor : cmdClass.getConstructors()) {
                if (matches(ctor.getParameterTypes(), args)) {
                    return (TransactionCommand) ctor.newInstance(args);
                }
            }
            throw new IllegalArgumentException("No matching constructor for " + className);
        } catch (Exception e) {
            throw new RuntimeException("Failed to build command for key: " + key, e);
        }
    }

    private static boolean matches(Class<?>[] paramTypes, Object[] args) {
        if (paramTypes.length != args.length) return false;
        for (int i = 0; i < paramTypes.length; i++) {
            if (args[i] == null) continue; // assume null fits
            if (!paramTypes[i].isAssignableFrom(args[i].getClass())) {
                return false;
            }
        }
        return true;
    }
}
