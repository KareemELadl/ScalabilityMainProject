package com.example.Transaction.command;

import com.example.Transaction.Model.Transaction;

public interface TransactionCommand {

    Transaction execute();
}
