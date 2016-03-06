package com.mazebert.gateways.transaction;

public interface TransactionManager {
    void runAsTransaction(Runnable runnable);

    Transaction startTransaction();
    Transaction getCurrent();
    void setCurrent(Transaction transaction);
}
