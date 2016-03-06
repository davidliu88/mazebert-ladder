package com.mazebert.gateways.transaction;

public interface TransactionRunner {
    void runAsTransaction(Runnable runnable);
}
