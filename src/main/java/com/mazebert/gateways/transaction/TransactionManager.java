package com.mazebert.gateways.transaction;

public interface TransactionManager extends TransactionRunner {
    Transaction startTransaction();
    Transaction getCurrent();
    void setCurrent(Transaction transaction);
}
